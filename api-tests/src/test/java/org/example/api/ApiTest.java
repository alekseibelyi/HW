package org.example.api;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("smoke")
public class ApiTest {

    private final BasicApi api = new BasicApi();

    @Test
    @Description("Смоук: Получение списка товаров")
    public void testGetGoodsList() {
        Response response = api.getGoodsList(0, 10);
        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertGoodsListNotNull(response);
    }

    @Test
    @Description("Смоук: Добавление товара")
    public void testAddGood() {
        String body = "{\"name\":\"TestItem_" + System.currentTimeMillis() + "\", \"price\":100}";

        // Явно передаем логин и пароль для гарантии авторизации
        Response response = io.restassured.RestAssured.given()
                .auth().preemptive().basic(org.example.TestConfig.getAdminLogin(), org.example.TestConfig.getAdminPassword())
                .contentType(io.restassured.http.ContentType.JSON)
                .body(body)
                .when()
                .post("/goods/add")
                .then()
                .extract().response();

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertResponseHasId(response);
    }
}