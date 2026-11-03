import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ConfigValidationTest {

    @BeforeAll
    public static void setup() {
        // Выводим конфиг в консоль перед запуском (кроме паролей)
        TestConfig.printConfig();

        // Настраиваем Selenide на основе конфига
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeout();

        // Настраиваем RestAssured на основе конфига
        RestAssured.baseURI = TestConfig.getApiUrl();
    }

    @Test
    public void testConfigUsage() {
        // 1. Проверяем доступность API, используя URL из конфига
        given()
                .auth().preemptive().basic(TestConfig.getAdminLogin(), TestConfig.getAdminPassword())
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue());

        // 2. Проверяем, что можем авторизоваться в UI
        open(TestConfig.getStandUrl() + "/admin");
        if (webdriver().driver().url().contains("login")) {
            $("#username").setValue(TestConfig.getAdminLogin());
            $("#password").setValue(TestConfig.getAdminPassword());
            $("button[type='submit']").click();
        }

        // 3. Убеждаемся, что админка открылась (видим кнопку добавления)
        $("#add-btn").shouldBe(com.codeborne.selenide.Condition.visible);
    }
}