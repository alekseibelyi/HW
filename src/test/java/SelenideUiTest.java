import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelenideUiTest {

    @BeforeAll
    public static void setUpConfig() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeout();

        // 1.6. Allure Selenide Listener
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @Step("Авторизация, если требуется")
    private void loginIfRequired() {
        if (webdriver().driver().url().contains("login")) {
            $("#username").setValue(TestConfig.getAdminLogin());
            $("#password").setValue(TestConfig.getAdminPassword());
            $("button[type='submit']").click();
        }
    }

    // 2.1.
    @Test
    @Description("Добавление товара через админку и проверка на витрине")
    public void testAddProductViaAdminAndCheckOnSite() {
        String productName = "Товар_" + new Random().nextInt(10000);

        open(TestConfig.getStandUrl() + "/admin");
        loginIfRequired();

        $("#n-name").setValue(productName);
        $("#n-price").setValue("150");
        $("#add-btn").click();

        open(TestConfig.getStandUrl() + "/");
        loginIfRequired();

        $(".product-card").shouldBe(visible);
        $(".products-grid").shouldHave(text(productName));
    }

    // 2.2.
    @Test
    @Description("Добавление товара в корзину и проверка отображения")
    public void testAddProductToCart() {
        open(TestConfig.getStandUrl() + "/");
        loginIfRequired();

        SelenideElement firstCard = $(".product-card").shouldBe(visible);
        String expectedName = firstCard.$("h4").getText();

        firstCard.$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").click();

        $(".cart-item b").shouldHave(text(expectedName));
    }

    // 2.3.
    @Test
    @Description("Попытка входа в админку с неверными данными")
    public void testLoginToAdminWithWrongCredentials() {
        open(TestConfig.getStandUrl() + "/login");

        $("#username").shouldBe(visible);

        $("#username").setValue("wrong_user");
        $("#password").setValue("wrong_password");
        $("button[type='submit']").click();

        $("#n-name").shouldNotBe(visible);
    }

    // 2.4.
    @Test
    @Description("Проверка сохранения корзины после обновления страницы")
    public void testCartSavedAfterRefresh() {
        open(TestConfig.getStandUrl() + "/");
        loginIfRequired();

        $(".product-card").shouldBe(visible).$("button[data-action='add-to-cart']").click();

        refresh();
        loginIfRequired();

        $("#open-cart-btn").click();

        $("#empty-cart").shouldNotBe(visible);
    }

    // 2.5.
    @Test
    @Description("Проверка JS Alert при заказе на сумму более 300 рублей")
    public void testOrderMoreThan300ShowsAlert() {
        open(TestConfig.getStandUrl() + "/");
        loginIfRequired();

        SelenideElement firstCard = $(".product-card").shouldBe(visible);
        SelenideElement addBtn = firstCard.$("button[data-action='add-to-cart']");
        SelenideElement qtyInput = firstCard.$(".qty-input");
        qtyInput.setValue("10");
        addBtn.click();
        $("#open-cart-btn").click();
        $("#makeOrder").click();
        Alert alert = switchTo().alert();
        String alertText = alert.getText();
        assertTrue(alertText.contains("Денег не хватает"), "Неверный текст алерта: " + alertText);
        assertTrue(alertText.contains("превышает лимит 300"), "Неверный текст алерта: " + alertText);
        alert.accept();
    }
}