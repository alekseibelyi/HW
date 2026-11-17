package org.example.api;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAssert {
    @Step("Проверить, что код ответа {expectedCode}")
    public static void assertStatusCode(Response response, int expectedCode) {
        assertThat(response.statusCode()).isEqualTo(expectedCode);
    }
}