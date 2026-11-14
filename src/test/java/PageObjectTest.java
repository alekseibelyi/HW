import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class PageObjectTest {

    private String testProductName = "Product_" + System.currentTimeMillis();

    @BeforeAll
    public static void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeout();
        RestAssured.baseURI = TestConfig.getApiUrl();
    }

    @AfterEach
    public void tearDown() {
        // Удаляем тестовый товар после каждого теста
        Integer id = given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .queryParam("page", 0).queryParam("size", 1000)
                .when().get("/goods/list")
                .then().statusCode(200)
                .extract().jsonPath().get("goods.find { it.name == '" + testProductName + "' }.id");

        if (id != null) {
            given().auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                    .when().delete("/goods/" + id)
                    .then().statusCode(anyOf(is(200), is(404)));
        }
    }

    @Test
    public void testPageObjectUsage() {
        // 1. Создаем товар через API, чтобы на витрине было что проверять
        given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + testProductName + "\", \"price\":100}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        // 2. Открываем страницу логина и проверяем элементы
        open(TestConfig.getStandUrl() + "/login");
        PageAssert.assertLoginPageIsVisible();

        LoginPage loginPage = new LoginPage();
        loginPage.enterUsername(TestConfig.getAdminLogin());
        PageAssert.assertUsernameHasText(TestConfig.getAdminLogin());
        loginPage.enterPassword(TestConfig.getAdminPassword()).clickSubmit();

        // 3. Переходим на витрину
        open(TestConfig.getStandUrl() + "/");
        refresh();

        MainPage mainPage = new MainPage();

        // 4. Проверяем, что наш товар виден
        PageAssert.assertProductIsVisible(testProductName);

        // 5. Кликаем "В корзину" и проверяем счетчик
        mainPage.clickAddToCart(testProductName);
        PageAssert.assertCartCountIs(1);
    }
}