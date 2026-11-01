import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Task3Test {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "secret123";

    private final List<String> createdProducts = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        RestAssured.baseURI = BASE_URL;
    }

    @AfterEach
    public void tearDown() {
        for (String productName : createdProducts) {
            deleteProductByName(productName);
        }
        createdProducts.clear();
    }

    private int createProductViaApi(String name, double price) {
        return given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\", \"price\":" + price + "}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract().jsonPath().getInt("data.id");
    }

    private void deleteProductByName(String name) {
        if (name == null) return;
        Integer id = given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .queryParam("page", 0).queryParam("size", 1000)
                .when().get("/goods/list")
                .then().statusCode(200)
                .extract().jsonPath().get("goods.find { it.name == '" + name + "' }.id");

        if (id != null) {
            given().auth().preemptive().basic(LOGIN, PASSWORD)
                    .when().delete("/goods/" + id)
                    .then().statusCode(anyOf(is(200), is(404)));
        }
    }

    private void loginToAdminIfRequired() {
        if (webdriver().driver().url().contains("login")) {
            $("#username").setValue(LOGIN);
            $("#password").setValue(PASSWORD);
            $("button[type='submit']").click();
        }
    }

    // 3.1. Добавить три единицы товара в корзину и оплатить

    @Test
    public void testBuyThreeItems() {
        String productName = "Товар_3_" + System.currentTimeMillis();
        createdProducts.add(productName);
        createProductViaApi(productName, 50.0);

        open(BASE_URL + "/");

        //  Обновляем страницу
        refresh();

        // Ищем карточку и добавляем
        $(".product-card").shouldHave(text(productName)).$(".qty-input").setValue("3");
        $(".product-card").shouldHave(text(productName)).$("button[data-action='add-to-cart']").click();

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

        open(BASE_URL + "/");
        refresh();

        // Ищем конкретную карточку через коллекцию и findBy
        $$(".product-card").findBy(text(product1)).$("button[data-action='add-to-cart']").click();
        $$(".product-card").findBy(text(product2)).$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").click();
        $("#total-price").shouldHave(text("35"));
    }

    // 3.3. Добавление товара в админке
    @Test
    public void testAddProductInAdmin() {
        String productName = "Товар_4_" + System.currentTimeMillis();
        createdProducts.add(productName);

        open(BASE_URL + "/admin");
        loginToAdminIfRequired();

        $("#n-name").setValue(productName);
        $("#n-price").setValue("200");
        $("#add-btn").click();


        $(".toast").shouldBe(visible).shouldHave(text("добавлен"));
    }

    // 3.4. Редактирование товара
    @Test
    public void testEditProduct() throws InterruptedException {
        String oldName = "Старый_товар_1_" + System.currentTimeMillis();
        String newName = "Новый_товар_1_" + System.currentTimeMillis();
        createdProducts.add(oldName);
        createdProducts.add(newName);

        // Создаем товар и получаем его ID
        int productId = createProductViaApi(oldName, 100.0);
        Thread.sleep(1000);

        open(BASE_URL + "/admin");
        loginToAdminIfRequired();
        refresh(); // Обновляем, чтобы данные подтянулись

        // 1. Ждем, пока таблица и tbody отрисуются (максимум 10 сек)
        $("tbody").shouldBe(visible);

        // 2. Находим строку по ID

        String xpathEditBtn = String.format("//tr[td[1][normalize-space()='%d']]//button[contains(@class, 'btn-upd')]", productId);

        $(By.xpath(xpathEditBtn)).click();

        // Меняем имя
        $("#n-name").setValue(newName);
        $("#add-btn").click();

        // Проверяем на витрине
        open(BASE_URL + "/");
        refresh();
        $(".products-grid").shouldHave(text(newName));
    }
}