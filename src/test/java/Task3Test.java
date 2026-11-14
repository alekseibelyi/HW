import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Task3Test {

    private final List<String> createdProducts = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeout();
        RestAssured.baseURI = TestConfig.getApiUrl();

        // Allure Listeners & Filters
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterEach
    @Step("Удалить созданные тестовые данные")
    public void tearDown() {
        for (String productName : createdProducts) {
            deleteProductByName(productName);
        }
        createdProducts.clear();
    }

    // ============================================================
    // Бизнес-методы и API-запросы (покрыты @Step)
    // ============================================================

    @Step("Создать товар через API: '{name}', цена {price}")
    private int createProductViaApi(String name, double price) {
        return given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\", \"price\":" + price + "}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract().jsonPath().getInt("data.id");
    }

    @Step("Удалить товар через API по имени: '{name}'")
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

    @Step("Авторизоваться в админке, если требуется")
    private void loginToAdminIfRequired() {
        if (webdriver().driver().url().contains("login")) {
            $("#username").setValue(TestConfig.getAdminLogin());
            $("#password").setValue(TestConfig.getAdminPassword());
            $("button[type='submit']").click();
        }
    }

    @Step("Обновить товар через API (ID: {id}, новое имя: '{newName}')")
    private void updateProductViaApi(int id, String newName, double price) {
        given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + newName + "\", \"price\":" + price + "}")
                .when()
                .patch("/goods/" + id)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    // ============================================================
    // Тесты
    // ============================================================

    // 3.1. Добавить три единицы товара в корзину и оплатить
    @Test
    public void testBuyThreeItems() throws InterruptedException {
        String productName = "Товар_" + System.currentTimeMillis();
        createdProducts.add(productName);

        double testPrice = 50.0;
        createProductViaApi(productName, testPrice);

        Thread.sleep(1000);

        open(TestConfig.getStandUrl() + "/");
        loginToAdminIfRequired();
        refresh();

        $(".products-grid").shouldHave(text(productName), exist);
        refresh();
        $(".products-grid").shouldHave(text(productName));

        SelenideElement myCard = $$(".product-card").findBy(text(productName));
        myCard.$(".qty-input").setValue("3");
        myCard.$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").click();
        $("#total-price").shouldHave(text("150"));
        $("#makeOrder").click();
        $("#main-title").shouldHave(text("Заказ успешно оформлен!"));
    }

    // 3.2. Несколько разных товаров, проверка суммы
    @Test
    public void testMultipleProductsSum() throws InterruptedException {
        String product1 = "Товар_1_A_" + System.currentTimeMillis();
        String product2 = "Товар_2_B_" + System.currentTimeMillis();
        createdProducts.add(product1);
        createdProducts.add(product2);

        createProductViaApi(product1, 10.0);
        createProductViaApi(product2, 25.0);

        Thread.sleep(1000);

        open(TestConfig.getStandUrl() + "/");
        refresh();

        $$(".product-card").findBy(text(product1)).$("button[data-action='add-to-cart']").click();
        $$(".product-card").findBy(text(product2)).$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").click();
        $("#total-price").shouldHave(text("35"));
    }

    // 3.3. Добавление товара в админке
    @Test
    public void testAddProductInAdmin() {
        String productName = "Товар__" + System.currentTimeMillis();
        createdProducts.add(productName);

        open(TestConfig.getStandUrl() + "/admin");
        loginToAdminIfRequired();

        $("#n-name").setValue(productName);
        $("#n-price").setValue("200");
        $("#add-btn").click();

        $(".toast").shouldBe(visible).shouldHave(text("добавлен"));
    }

    // 3.4. Редактирование товара
    @Test
    public void testEditProduct() throws InterruptedException {
        String oldName = "Старый_товар_" + System.currentTimeMillis();
        String newName = "Новый_товар_" + System.currentTimeMillis();
        createdProducts.add(oldName);
        createdProducts.add(newName);

        // Создаем через API (возвращает ID)
        int productId = createProductViaApi(oldName, 100.0);
        Thread.sleep(1000);

        // 1. Входим в админку
        open(TestConfig.getStandUrl() + "/admin");
        loginToAdminIfRequired();
        refresh();
        $("tbody").shouldBe(visible);

        // 2. Обновляем товар через API
        updateProductViaApi(productId, newName, 100.0);
        Thread.sleep(1000);

        // 3. Выходим на витрину и проверяем изменения
        open(TestConfig.getStandUrl() + "/");
        refresh();

        $(".products-grid").shouldHave(text(newName));
        $(".products-grid").shouldNotHave(text(oldName));
    }
}