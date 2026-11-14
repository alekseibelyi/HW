import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiEndpointsTest {

    private static final String LOGIN = "admin";
    private static final String PASSWORD = "secret123";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = TestConfig.getApiUrl();
        // 1.7. AllureRestAssured Filter
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterEach
    @Step("Очистить все товары через API")
    public void tearDown() {
        List<Integer> ids = given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("goods.id");

        if (ids != null) {
            for (Integer id : ids) {
                given()
                        .auth().preemptive().basic(LOGIN, PASSWORD)
                        .when()
                        .delete("/goods/" + id)
                        .then()
                        .statusCode(anyOf(is(200), is(404)));
            }
        }
    }

    // ============================================================
    // 1.1. Шаги для запросов к эндпоинтам
    // ============================================================

    @Step("Создать товар через API: имя '{name}', цена {price}")
    private long createItemAndGetId(String name, double price) {
        return given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\", \"price\":" + price + "}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract().jsonPath().getLong("data.id");
    }

    @Step("Отправить POST /goods/add с телом '{body}'")
    private Response postGood(String body) {
        return given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/goods/add")
                .then()
                .extract().response();
    }

    @Step("Отправить GET /goods/{id} для ID {id}")
    private Response getGoodById(long id) {
        return given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .when()
                .get("/goods/" + id)
                .then()
                .extract().response();
    }

    @Step("Отправить DELETE /goods/{id} для ID {id}")
    private Response deleteGood(long id) {
        return given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .when()
                .delete("/goods/" + id)
                .then()
                .extract().response();
    }

    @Step("Отправить PATCH /goods/{id} с телом '{body}'")
    private Response patchGood(long id, String body) {
        return given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch("/goods/" + id)
                .then()
                .extract().response();
    }

    @Step("Получить список товаров (page={page}, size={size})")
    private Response getGoodsList(int page, int size) {
        return given()
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/goods/list")
                .then()
                .extract().response();
    }

    // ============================================================
    // 1.2. Шаги для API-проверок
    // ============================================================

    @Step("Проверить, что код ответа равен {expectedCode}")
    private void assertStatusCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @Step("Проверить, что в ответе есть data.id")
    private void assertResponseHasId(Response response) {
        response.then().body("data.id", notNullValue());
    }

    @Step("Проверить, что ID в ответе равен {expectedId}")
    private void assertResponseId(Response response, long expectedId) {
        response.then().body("id", equalTo((int) expectedId));
    }

    @Step("Проверить, что список товаров в ответе не пустой")
    private void assertGoodsListNotNull(Response response) {
        response.then().body("goods", notNullValue());
    }


    // ============================================================
    // ТЕСТЫ
    // ============================================================

    // 1. POST /goods/add
    @Test
    public void testAddGood_Positive() {
        Response response = postGood("{\"name\":\"Мышка\", \"price\":1500.50}");
        assertStatusCode(response, 200);
        assertResponseHasId(response);
    }

    @Test
    public void testAddGood_BadRequest() {
        Response response = postGood("{\"name\":\"\", \"price\":-10}");
        assertStatusCode(response, 400);
    }

    // 2. GET /goods/{id}
    @Test
    public void testGetGoodById_Positive() {
        long id = createItemAndGetId("Клавиатура", 3000);
        Response response = getGoodById(id);
        assertStatusCode(response, 200);
        assertResponseId(response, id);
    }

    @Test
    public void testGetGoodById_NotFound() {
        Response response = getGoodById(-1);
        assertStatusCode(response, 404);
    }

    // 3. DELETE /goods/{id}
    @Test
    public void testDeleteGood_Positive() {
        long id = createItemAndGetId("Временный товар", 100);
        Response response = deleteGood(id);
        assertStatusCode(response, 200);
    }

    @Test
    public void testDeleteGood_NotFound() {
        Response response = deleteGood(-1);
        assertStatusCode(response, 404);
    }

    // 4. PATCH /goods/{id}
    @Test
    public void testPatchGood_Positive() {
        long id = createItemAndGetId("Старое имя", 500);
        Response response = patchGood(id, "{\"name\":\"Новое имя\", \"price\":600}");
        assertStatusCode(response, 200);
    }

    @Test
    public void testPatchGood_BadRequest() {
        long id = createItemAndGetId("Товар для патча", 500);
        Response response = patchGood(id, "{\"name\":\"\", \"price\":-5}");
        assertStatusCode(response, 400);
    }

    @Test
    public void testPatchGood_NotFound() {
        Response response = patchGood(-1, "{\"name\":\"Имя\", \"price\":100}");
        assertStatusCode(response, 404);
    }

    // 5. GET /goods/list
    @Test
    public void testGetGoodsListWithPagination() {
        Response response = getGoodsList(0, 5);
        assertStatusCode(response, 200);
        assertGoodsListNotNull(response);
    }
}