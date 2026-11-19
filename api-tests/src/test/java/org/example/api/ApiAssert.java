package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class ApiAssert {

    @Step("Проверить, что код ответа равен {expectedCode}")
    public static void assertStatusCode(Response response, int expectedCode) {
        assertThat(response.statusCode()).isEqualTo(expectedCode);
    }

    @Step("Проверить, что в ответе есть поле data.id")
    public static void assertResponseHasId(Response response) {
        response.then().body("data.id", notNullValue());
    }

    @Step("Проверить, что ID в ответе равен {expectedId}")
    public static void assertResponseId(Response response, long expectedId) {
        response.then().body("id", org.hamcrest.Matchers.equalTo((int) expectedId));
    }

    @Step("Проверить, что список товаров не пустой")
    public static void assertGoodsListNotNull(Response response) {
        response.then().body("goods", notNullValue());
    }
}