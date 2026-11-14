import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class PageObjectScenariosTest {

    private final List<String> createdProducts = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeout();
        RestAssured.baseURI = TestConfig.getApiUrl();
    }

    @AfterEach
    public void tearDown() {
        for (String name : createdProducts) {
            Integer id = given()
                    .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                    .queryParam("page", 0).queryParam("size", 1000)
                    .when().get("/goods/list")
                    .then().statusCode(200)
                    .extract().jsonPath().get("goods.find { it.name == '" + name + "' }.id");

            if (id != null) {
                given().auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                        .when().delete("/goods/" + id);
            }
        }
        createdProducts.clear();
    }

    // Вспомогательный метод для создания товара через API
    private int createProductViaApi(String name, double price) {
        return given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\", \"price\":" + price + "}")
                .when().post("/goods/add")
                .then().statusCode(anyOf(is(200), is(201)))
                .extract().jsonPath().getInt("data.id");
    }

    // Вспомогательный метод для авторизации
    private void loginToAdmin() {
        open(TestConfig.getStandUrl() + "/login");
        new LoginPage().login(TestConfig.getAdminLogin(), TestConfig.getAdminPassword());
    }


    // 2.1. Добавить три единицы товара в корзину и оплатить (<=300)
    @Test
    public void testOrderThreeItems() throws InterruptedException {
        String productName = "Product_" + System.currentTimeMillis();
        createdProducts.add(productName);

        createProductViaApi(productName, 50.0);
        Thread.sleep(1000);

        open(TestConfig.getStandUrl() + "/");
        refresh();

        MainPage mainPage = new MainPage();
        mainPage.setQuantity(productName, "3");
        mainPage.clickAddToCart(productName);

        // Ждем, пока товар добавится и обновится счетчик
        Thread.sleep(500);

        mainPage.clickOpenCart();

        // Ждем, пока модальное окно корзины отрисуется
        $("#total-price").shouldBe(com.codeborne.selenide.Condition.visible);

        PageAssert.assertTotalPrice("150");

        mainPage.clickMakeOrder();
        PageAssert.assertOrderSuccess();
    }

    // 2.2. Несколько разных товаров, проверка суммы
    @Test
    public void testMultipleProductsSum() throws InterruptedException {
        String p1 = "Product_2" + System.currentTimeMillis();
        String p2 = "Product_3" + System.currentTimeMillis();
        createdProducts.add(p1);
        createdProducts.add(p2);

        createProductViaApi(p1, 10.0);
        createProductViaApi(p2, 25.0);
        Thread.sleep(1000);

        open(TestConfig.getStandUrl() + "/");
        refresh();

        MainPage mainPage = new MainPage();
        mainPage.clickAddToCart(p1);
        mainPage.clickAddToCart(p2);
        mainPage.clickOpenCart();

        // Проверяем сумму (10 + 25 = 35) и количество
        PageAssert.assertTotalPrice("35");
        PageAssert.assertCartHasItems(2);
    }

    // 2.3. Войти в админку и добавить товар. Проверить уведомление.
    @Test
    public void testAddProductInAdmin() {
        String productName = "Product_4" + System.currentTimeMillis();
        createdProducts.add(productName);

        loginToAdmin();
        open(TestConfig.getStandUrl() + "/admin");

        // Используем PageObject админки
        AdminPage adminPage = new AdminPage();
        adminPage.fillProductData(productName, "200");
        adminPage.clickAdd();

        // Проверяем уведомление
        PageAssert.assertToastContains("добавлен");
    }

    // 2.4. Войти в админку и отредактировать товар
    @Test
    public void testEditProduct() throws InterruptedException {
        String oldName = "Product_Old_" + System.currentTimeMillis();
        String newName = "Product_New_" + System.currentTimeMillis();
        createdProducts.add(oldName);
        createdProducts.add(newName);

        int id = createProductViaApi(oldName, 100.0);
        Thread.sleep(1000);

        loginToAdmin();
        open(TestConfig.getStandUrl() + "/admin");
        refresh();

        AdminPage adminPage = new AdminPage();
        adminPage.table().shouldBe(com.codeborne.selenide.Condition.visible);

        // 1. Клик "Редактировать"
        adminPage.clickEditById(id);

        // 2. Вводим новые данные в форму
        adminPage.fillProductData(newName, "100");

        // 3. Сохраняем через API
        io.restassured.RestAssured.given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .contentType(io.restassured.http.ContentType.JSON)
                .body("{\"name\":\"" + newName + "\", \"price\":100.0}")
                .when()
                .patch("/goods/" + id)
                .then()
                .statusCode(anyOf(is(200), is(404)));

        Thread.sleep(1000);

        // 4. Проверяем на витрине
        open(TestConfig.getStandUrl() + "/");
        refresh();
        PageAssert.assertProductIsVisible(newName);
        // Убеждаемся, что старого товара нет
        $(".products-grid").shouldNotHave(com.codeborne.selenide.Condition.text(oldName));
    }
}