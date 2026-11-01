import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SelenideUiTest {

    private static final String SITE_URL = "http://localhost:8080/";
    private static final String ADMIN_URL = "http://localhost:8080/admin";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "secret123";

    @BeforeAll
    public static void setUpConfig() {
        // Настройка Selenide
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000; // Таймаут ожидания элементов (10 сек)
    }

    // Вспомогательный метод для авторизации
    private void loginIfRequired() {
        if (webdriver().driver().url().contains("login")) {
            $("#username").setValue(LOGIN);
            $("#password").setValue(PASSWORD);
            $("button[type='submit']").click();
        }
    }

    // 2.1. Добавить товар через админку, выйти на витрину и проверить
    @Test
    public void testAddProductViaAdminAndCheckOnSite() {
        String productName = "Товар_" + new Random().nextInt(10000);

        // 1. Идем в админку и авторизуемся
        open(ADMIN_URL);
        loginIfRequired();

        // 2. Добавляем товар
        $("#n-name").setValue(productName);
        $("#n-price").setValue("150");
        $("#add-btn").click();

        // 3. Идем на витрину
        open(SITE_URL);
        loginIfRequired();

        // 4. ЖДЕМ загрузки карточки товара
        $(".product-card").shouldBe(visible);

        // 5. Проверяем, что товар отображается
        $(".products-grid").shouldHave(text(productName));
    }

    // 2.2. Добавить товар в корзину и проверить, что он отображается
    @Test
    public void testAddProductToCart() {
        // 1. Открываем витрину
        open(SITE_URL);
        loginIfRequired();

        // 2. Ждем загрузки первой карточки
        SelenideElement firstCard = $(".product-card").shouldBe(visible);
        String expectedName = firstCard.$("h4").getText();

        // 3. Нажимаем кнопку "В корзину"
        firstCard.$("button[data-action='add-to-cart']").click();

        // 4. Открываем корзину
        $("#open-cart-btn").click();

        // 5. Проверяем, что товар появился в корзине
        $(".cart-item b").shouldHave(text(expectedName));
    }

    // 2.3. Попытаться войти в админку с неверным логином и паролем
    @Test
    public void testLoginToAdminWithWrongCredentials() {
        // Открываем страницу логина напрямую
        open("http://localhost:8080/login");

        // Ждем, пока загрузится поле ввода логина
        $("#username").shouldBe(visible);

        // Вводим неверные данные
        $("#username").setValue("wrong_user");
        $("#password").setValue("wrong_password");
        $("button[type='submit']").click();

        // Проверяем, что мы не попали в админку (элемента n-name нет)
        $("#n-name").shouldNotBe(visible);
    }

    // 2.4. Проверить сохранение товаров в корзине после обновления страницы
    @Test
    public void testCartSavedAfterRefresh() {
        open(SITE_URL);
        loginIfRequired();

        // Добавляем товар в корзину
        $(".product-card").shouldBe(visible).$("button[data-action='add-to-cart']").click();

        // Обновляем страницу
        refresh();
        loginIfRequired();

        // Открываем корзину
        $("#open-cart-btn").click();

        // Проверяем, что корзина не пуста (нет элемента "Пусто")
        $("#empty-cart").shouldNotBe(visible);
    }

    // 2.5. Добавить товаров более чем на 300 рублей и проверить JS Alert
    @Test
    public void testOrderMoreThan300ShowsAlert() {
        open(SITE_URL);
        loginIfRequired();

        // Находим все карточки и кнопки добавления в корзину
        // Для надежности добавим первый товар 3 раза (или найдем товар дороже 300)
        SelenideElement firstCard = $(".product-card").shouldBe(visible);
        SelenideElement addBtn = firstCard.$("button[data-action='add-to-cart']");
        SelenideElement qtyInput = firstCard.$(".qty-input");

        // Увеличим количество до 10, чтобы точно превысить 300 рублей
        qtyInput.setValue("10");
        addBtn.click();

        // Открываем корзину и жмем "Оформить заказ"
        $("#open-cart-btn").click();
        $("#makeOrder").click();

        // Проверяем, что появился JS Alert
        Alert alert = switchTo().alert();
        String alertText = alert.getText();

        // Проверяем текст алерта
        assert alertText.contains("Денег не хватает") : "Неверный текст алерта: " + alertText;
        assert alertText.contains("превышает лимит 300") : "Неверный текст алерта: " + alertText;

        // Закрываем алерт, чтобы тест не завис
        alert.accept();
    }
}