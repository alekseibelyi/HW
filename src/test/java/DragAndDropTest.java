import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DragAndDropTest {

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

    private void createProductViaApi(String name, double price) {
        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\", \"price\":" + price + "}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(anyOf(is(200), is(201)));
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

    private void loginIfRequired() {
        if (webdriver().driver().url().contains("login")) {
            $("#username").setValue(LOGIN);
            $("#password").setValue(PASSWORD);
            $("button[type='submit']").click();
        }
    }


    // 1.1. Перетащить элемент в корзину с помощью Drag-and-Drop

    @Test
    public void testDragAndDropToCart() throws InterruptedException {
        String productName = "Товар_" + System.currentTimeMillis();
        createdProducts.add(productName);
        createProductViaApi(productName, 100.0);
        Thread.sleep(1000);

        open(BASE_URL + "/");
        loginIfRequired();
        refresh();

        $(".product-card").shouldBe(visible);
        SelenideElement sourceCard = $$(".product-card").findBy(text(productName));

        SelenideElement targetCart = $("#open-cart-btn");

        actions().dragAndDrop(sourceCard, targetCart).perform();
        targetCart.click();

        $(".cart-item").shouldBe(visible).shouldHave(text(productName));
    }

    // 1.2. Удалить добавленный элемент из корзины и проверить, что его нет
    @Test
    public void testRemoveItemFromCart() throws InterruptedException {
        String productName = "Другой_Товар_" + System.currentTimeMillis();
        createdProducts.add(productName);
        createProductViaApi(productName, 50.0);
        Thread.sleep(1000);

        open(BASE_URL + "/");
        loginIfRequired();
        refresh();

        $(".product-card").shouldBe(visible);
        SelenideElement card = $$(".product-card").findBy(text(productName));
        card.$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").click();

        SelenideElement cartItem = $(".cart-item").shouldHave(text(productName));

        cartItem.$("button[data-action='remove']").click();

        // 5. Проверяем, что товар исчез из корзины
        $("#cart-items").shouldNotHave(text(productName));
        $("#empty-cart").shouldBe(visible);
    }
}