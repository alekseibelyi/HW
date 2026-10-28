import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class ApiTests {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "secret123";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.authentication = RestAssured.preemptive().basic(LOGIN, PASSWORD);
    }


    // Метод 1. given() / when() / then()
    @Test
    public void testGetGoodsListWithGivenWhenThen() {
        given()
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
        }

    // Метод 2. RequestSpecification
    @Test
    public void testGetGoodsListWithRequestSpec() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .build();

        given()
                .spec(spec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    // Метод 3. POST + GET через встроенные проверки Rest Assured
    @Test
    public void testAddGoodAndCheckInListWithRestAssured() throws InterruptedException {
        String goodName = "test_good_" + System.currentTimeMillis();

        // 1. Создаем товар
        given()
                .contentType("application/json")
                .body("{\"name\":\"" + goodName + "\", \"price\":100}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(Matchers.anyOf(Matchers.is(200), Matchers.is(201)));

        Thread.sleep(1000);

        // 2. Проверяем в списке
        given()
                .queryParam("page", 0)
                .queryParam("size", 100)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods.name", Matchers.hasItem(goodName));
    }

    // Метод 4. POST + GET через AssertJ
    @Test
    public void testAddGoodAndCheckInListWithAssertJ() throws InterruptedException {
        String goodName = "test_good_" + System.currentTimeMillis();

        // 1. Создаем товар
        given()
                .contentType("application/json")
                .body("{\"name\":\"" + goodName + "\", \"price\":100}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(Matchers.anyOf(Matchers.is(200), Matchers.is(201)));

        Thread.sleep(1000);

        // 2. Получаем список
        Response response = given()
                .queryParam("page", 0)
                .queryParam("size", 100)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods.name", Matchers.hasItem(goodName))
                .extract().response();

        // 3. Проверяем через AssertJ
        List<String> names = response.jsonPath().getList("goods.name");
        assertThat(names)
                .as("Список товаров должен содержать '%s'", goodName)
                .contains(goodName);
    }
}