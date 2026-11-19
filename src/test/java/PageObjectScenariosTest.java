import com.codeborne.selenide.Configuration;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class PageObjectScenariosTest {

    private final List<String> createdProducts = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeout();
        io.restassured.RestAssured.baseURI = TestConfig.getApiUrl();
    }

    @AfterEach
    @Step("Удалить созданные тестовые данные")
    public void tearDown() {
        for (String name : createdProducts) {
            deleteProductByName(name);
        }
        createdProducts.clear();
    }

    @Step("Создать товар через API: '{name}', цена {price}")
    private int createProductViaApi(String name, double price) {
        return given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .contentType(io.restassured.http.ContentType.JSON)
                .body("{\"name\":\"" + name + "\", \"price\":" + price + "}")
                .when().post("/goods/add")
                .then().statusCode(anyOf(is(200), is(201)))
                .extract().jsonPath().getInt("data.id");
    }

    @Step("Удалить товар '{name}'")
    private void deleteProductByName(String name) {
        if (name == null) return;
        Integer id = given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .queryParam("page", 0).queryParam("size", 1000)
                .when().get("/goods/list")
                .then().statusCode(200)
                .extract().jsonPath().get("goods.find { it.name == '" + name + "' }.id");

        if (id != null) {
            given().auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                    .when().delete("/goods/" + id)
                    .then().statusCode(anyOf(is(200), is(404)));
        }
    }

    @Step("Авторизоваться в админке")
    private void loginToAdmin() {
        open(TestConfig.getStandUrl() + "/login");
        new LoginPage().login(TestConfig.getAdminLogin(), TestConfig.getAdminPassword());
    }


    // 2.1. Оплата трех товаров

    @Test
    @Description("Оплата трех товаров (цена < 100)")
    public void testOrderThreeItems() throws InterruptedException {
        String productName = "PO_Item_31_" + System.currentTimeMillis();
        createdProducts.add(productName);
        createProductViaApi(productName, 50.0);
        Thread.sleep(1000);

        open(TestConfig.getStandUrl() + "/");
        PageAssert.assertMainPageIsLoaded();

        MainPage mainPage = new MainPage();
        mainPage.setQuantity(productName, "3");
        mainPage.clickAddToCart(productName);
        mainPage.clickOpenCart();

        PageAssert.assertTotalPrice("150");
        mainPage.clickMakeOrder();
        PageAssert.assertOrderSuccess();
    }


    // 2.2. Сумма нескольких товаров

    @Test
    @Description("Проверка суммы нескольких товаров")
    public void testMultipleProductsSum() throws InterruptedException {
        String p1 = "PO_Item_32_A_" + System.currentTimeMillis();
        String p2 = "PO_Item_32_B_" + System.currentTimeMillis();
        createdProducts.add(p1);
        createdProducts.add(p2);

        createProductViaApi(p1, 10.0);
        createProductViaApi(p2, 25.0);
        Thread.sleep(1000);

        open(TestConfig.getStandUrl() + "/");
        PageAssert.assertMainPageIsLoaded();

        MainPage mainPage = new MainPage();
        mainPage.clickAddToCart(p1);
        mainPage.clickAddToCart(p2);
        mainPage.clickOpenCart();

        PageAssert.assertTotalPrice("35");
        PageAssert.assertCartHasItems(2);
    }


    // 2.3. Добавление товара в админке

    @Test
    @Description("Добавление товара в админке и проверка уведомления")
    public void testAddProductInAdmin() {
        String productName = "PO_Item_33_" + System.currentTimeMillis();
        createdProducts.add(productName);

        loginToAdmin();
        open(TestConfig.getStandUrl() + "/admin");
        PageAssert.assertAdminPageIsLoaded();

        AdminPage adminPage = new AdminPage();
        adminPage.fillProductData(productName, "200");
        adminPage.clickAdd();

        PageAssert.assertToastContains("добавлен");
    }


    // 2.4. Редактирование товара (через UI!)

    @Test
    @Description("Редактирование товара в админке и проверка на витрине")
    public void testEditProduct() throws InterruptedException {
        String oldName = "PO_Old_34_" + System.currentTimeMillis();
        String newName = "PO_New_34_" + System.currentTimeMillis();
        createdProducts.add(oldName);
        createdProducts.add(newName);

        int id = createProductViaApi(oldName, 100.0);
        Thread.sleep(1000);

        loginToAdmin();
        open(TestConfig.getStandUrl() + "/admin");
        refresh();
        PageAssert.assertAdminPageIsLoaded();

        // Редактируем через UI (метод в AdminPage)
        AdminPage adminPage = new AdminPage();
        adminPage.editProductInTable(id, newName, "150");

        Thread.sleep(1000);

        // Проверяем на витрине
        open(TestConfig.getStandUrl() + "/");
        refresh();
        PageAssert.assertProductVisibleInGrid(newName);
        PageAssert.assertProductNotVisibleInGrid(oldName);
    }
}