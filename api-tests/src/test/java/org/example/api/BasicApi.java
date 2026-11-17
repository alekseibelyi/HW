package org.example.api;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BasicApi extends RestApiBuilder {

    @Step("Отправить GET /goods/list")
    public Response getGoodsList() {
        return given().spec(getBaseSpec()).get("/goods/list");
    }

    @Step("Отправить POST /goods/add с телом '{body}'")
    public Response addGood(String body) {
        return given().spec(getBaseSpec()).body(body).post("/goods/add");
    }
}