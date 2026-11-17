import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.example.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ConfigValidationTest {

    @BeforeAll
    public static void setup() {
        TestConfig.printConfig();
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeout();

        RestAssured.baseURI = TestConfig.getApiUrl();
    }

    @Test
    public void testConfigUsage() {
        given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue());

        open(TestConfig.getStandUrl() + "/admin");
        if (webdriver().driver().url().contains("login")) {
            $("#username").setValue(TestConfig.getAdminLogin());
            $("#password").setValue(TestConfig.getAdminPassword());
            $("button[type='submit']").click();
        }
        $("#add-btn").shouldBe(com.codeborne.selenide.Condition.visible);
    }
}