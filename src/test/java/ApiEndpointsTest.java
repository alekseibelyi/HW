import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.AfterEach;

import java.util.List;

public class ApiEndpointsTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "secret123";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @AfterEach
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
                        .statusCode(anyOf(is(200), is(404))); // Удаляем, если есть
            }
        }
    }

   // Вспомогательный метод: создает товар и возвращает его ID
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

    // 1. POST /goods/add (Коды: 200, 400)
    @Test
    public void testAddGood_Positive() {
        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Мышка\", \"price\":1500.50}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .body("data.id", notNullValue());
    }

    @Test
    public void testAddGood_BadRequest() {
        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"\", \"price\":-10}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(400);
    }

    // 2. GET /goods/{id} (Коды: 200, 404)
    @Test
    public void testGetGoodById_Positive() {
        long id = createItemAndGetId("Клавиатура", 3000);

        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .when()
                .get("/goods/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo((int) id));
    }

    @Test
    public void testGetGoodById_NotFound() {
        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .when()
                .get("/goods/-1") // Несуществующий ID
                .then()
                .statusCode(404);
    }

    // 3. DELETE /goods/{id} (Коды: 200, 404)
    @Test
    public void testDeleteGood_Positive() {
        long id = createItemAndGetId("Временный товар", 100);

        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .when()
                .delete("/goods/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteGood_NotFound() {
        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .when()
                .delete("/goods/-1")
                .then()
                .statusCode(404);
    }

    // 4. PATCH /goods/{id} (Коды: 200, 400, 404)
    @Test
    public void testPatchGood_Positive() {
        long id = createItemAndGetId("Старое имя", 500);

        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Новое имя\", \"price\":600}")
                .when()
                .patch("/goods/" + id)
                .then()
                .statusCode(200);
    }
    @Test
    public void testPatchGood_BadRequest() {
        long id = createItemAndGetId("Товар для патча", 500);

        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"\", \"price\":-5}")
                .when()
                .patch("/goods/" + id)
                .then()
                .statusCode(400);
    }
    @Test
    public void testPatchGood_NotFound() {
        given()
                .auth().preemptive().basic(LOGIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Имя\", \"price\":100}")
                .when()
                .patch("/goods/-1")
                .then()
                .statusCode(404);
    }
    // 5. GET /goods/list (Коды: 200)
    @Test
    public void testGetGoodsListWithPagination() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue());
    }
}