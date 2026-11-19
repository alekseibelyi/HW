package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BasicApi extends RestApiBuilder {

    @Step("GET /goods/list")
    public Response getGoodsList(int page, int size) {
        return given().spec(getBaseSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .get("/goods/list");
    }

    @Step("POST /goods/add")
    public Response addGood(String body) {
        return given().spec(getBaseSpec()).body(body).post("/goods/add");
    }

    @Step("GET /goods/{id}")
    public Response getGoodById(long id) {
        return given().spec(getBaseSpec()).get("/goods/" + id);
    }

    @Step("DELETE /goods/{id}")
    public Response deleteGood(long id) {
        return given().spec(getBaseSpec()).delete("/goods/" + id);
    }

    @Step("PATCH /goods/{id}")
    public Response patchGood(long id, String body) {
        return given().spec(getBaseSpec()).body(body).patch("/goods/" + id);
    }
}