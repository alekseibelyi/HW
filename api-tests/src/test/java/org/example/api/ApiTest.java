package org.example.api;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("smoke")
public class ApiTest {@BeforeAll
public static void setup() {
    RestAssured.baseURI = TestConfig.getApiUrl();

    // 1.7. AllureRestAssured Filter
    RestAssured.filters(new AllureRestAssured());
}

    // 1.1. Методы для обращения к эндпоинтам
    @Step("Получить список товаров (GET /goods/list)")
    private Response getGoodsList() {
        return given()
                .when()
                .get("/goods/list")
                .then()
                .extract().response();
    }

    @Step("Получить список товаров с пагинацией (GET /goods/list?page={page}&size={size})")
    private Response getGoodsList(int page, int size) {
        return given()
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/goods/list")
                .then()
                .extract().response();
    }

    @Step("Создать товар (POST /goods/add) с именем '{name}' и ценой {price}")
    private void createGood(String name, int price) {
        given()
                .contentType("application/json")
                .body("{\"name\":\"" + name + "\", \"price\":" + price + "}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(Matchers.anyOf(Matchers.is(200), Matchers.is(201)));
    }

    // 1.2. API-проверки (покрыты @Step)

    @Step("Проверить, что код ответа равен {expectedCode}")
    private void assertStatusCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @Step("Проверить, что список товаров пуст")
    private void assertGoodsListIsEmpty(Response response) {
        response.then().body("goods", empty());
    }

    @Step("Проверить, что в списке есть товар '{goodName}'")
    private void assertGoodsListContains(Response response, String goodName) {
        response.then().body("goods.name", Matchers.hasItem(goodName));
    }

    // ТЕСТЫ

    // Метод 1. given() / when() / then()
    @Test
    public void testGetGoodsListWithGivenWhenThen() {
        Response response = getGoodsList();
        assertStatusCode(response, 200);
        assertGoodsListIsEmpty(response);
    }
    // Метод 2. RequestSpecification
    @Test
    public void testGetGoodsListWithRequestSpec() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(TestConfig.getApiUrl())
                .build();

        Response response = given()
                .spec(spec)
                .when()
                .get("/goods/list")
                .then()
                .extract().response();

        assertStatusCode(response, 200);
        assertGoodsListIsEmpty(response);
    }

    // Метод 3. POST + GET через встроенные проверки Rest Assured
    @Test
    public void testAddGoodAndCheckInListWithRestAssured() throws InterruptedException {
        String goodName = "test_good_" + System.currentTimeMillis();

        createGood(goodName, 100);
        Thread.sleep(1000);

        Response response = getGoodsList(0, 100);
        assertStatusCode(response, 200);
        assertGoodsListContains(response, goodName);
    }

    // Метод 4. POST + GET через AssertJ
    @Test
    public void testAddGoodAndCheckInListWithAssertJ() throws InterruptedException {
        String goodName = "test_good_" + System.currentTimeMillis();

        createGood(goodName, 100);
        Thread.sleep(1000);

        Response response = getGoodsList(0, 100);
        assertStatusCode(response, 200);

        // Проверка через AssertJ
        List<String> names = response.jsonPath().getList("goods.name");
        assertThat(names)
                .as("Список товаров должен содержать '%s'", goodName)
                .contains(goodName);
    }
}