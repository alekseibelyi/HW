import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SeleniumUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String SITE_URL = "http://localhost:8080/";
    private static final String ADMIN_URL = "http://localhost:8080/admin";

    private static final String LOGIN = "admin";
    private static final String PASSWORD = "secret123";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    // Вспомогательный метод для авторизации в Spring Security
    private void loginIfRequired() {
        // Если в URL есть слово "login" — значит, нас выкинуло на страницу входа
        if (driver.getCurrentUrl().contains("login")) {
            // Ждем поля ввода (стандартные id в Spring Security: username и password)
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));

            usernameField.sendKeys(LOGIN);
            passwordField.sendKeys(PASSWORD);
            submitBtn.click();
        }
    }

    // ============================================================
    // 1.1. Добавить товар через админку, выйти на витрину и проверить
    // ============================================================
    @Test
    public void testAddProductViaAdminAndCheckOnSite() {
        String productName = "Товар_" + new Random().nextInt(10);

        // 1. Идем в админку
        driver.get(ADMIN_URL);

        // 2. Авторизуемся, если выкинуло на /login
        loginIfRequired();

        // 3. Ждем загрузки поля "Название" (теперь мы в админке)
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("n-name")));
        WebElement priceInput = driver.findElement(By.id("n-price"));
        WebElement addBtn = driver.findElement(By.id("add-btn"));

        // 4. Заполняем и добавляем
        nameInput.sendKeys(productName);
        priceInput.sendKeys("150");
        addBtn.click();

        // 5. Идем на витрину
        driver.get(SITE_URL);
        loginIfRequired(); // Если витрина тоже закрыта, авторизуемся

        // 6. Проверяем, что товар отображается на витрине
        boolean isProductDisplayed = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".products-grid"), productName
        ));
        assertTrue(isProductDisplayed, "Товар '" + productName + "' не найден на витрине!");
    }

    // ============================================================
    // 1.2. Добавить товар в корзину и проверить, что он отображается
    // ============================================================
    @Test
    public void testAddProductToCart() {
        // 1. Открываем витрину
        driver.get(SITE_URL);
        loginIfRequired(); // Авторизуемся, если нужно

        // 2. Ждем загрузки первой карточки и запоминаем её название
        WebElement firstCardTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-card h4")
        ));
        String expectedName = firstCardTitle.getText();

        // 3. Нажимаем кнопку "В корзину"
        WebElement addToCartBtn = driver.findElement(By.cssSelector(".product-card button[data-action='add-to-cart']"));
        addToCartBtn.click();

        // 4. Открываем корзину
        WebElement openCartBtn = driver.findElement(By.id("open-cart-btn"));
        openCartBtn.click();

        // 5. Проверяем, что товар появился в корзине
        WebElement cartItemName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".cart-item b")
        ));
        assertEquals(expectedName, cartItemName.getText(), "Название товара в корзине не совпадает!");
    }

    // ============================================================
    // 1.3. Попытаться войти в админку с неверным логином и паролем
    // ============================================================
    @Test
    public void testLoginToAdminWithWrongCredentials() {
        driver.get(ADMIN_URL);

        // Ждем поля логина, но НЕ используем наш метод (вводим неверные данные)
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("wrong_user");
        passwordField.sendKeys("wrong_password");
        submitBtn.click();

        // После неверного ввода Spring Security вернет нас обратно на страницу логина (часто с параметром ?error)
        // Проверяем, что мы все еще на странице логина
        wait.until(ExpectedConditions.urlContains("login"));

        // Проверяем наличие сообщения об ошибке (Spring обычно добавляет .alert или div с текстом "Bad credentials")
        // Если не знаете точный класс ошибки, просто проверяем, что мы не в админке
        boolean isAdminLoaded = driver.getPageSource().contains("n-name");
        assertFalse(isAdminLoaded, "Ошибка: удалось зайти в админку с неверным паролем!");
    }

    // ============================================================
    // 1.4. Проверить сохранение товаров в корзине после обновления страницы
    // ============================================================
    @Test
    public void testCartSavedAfterRefresh() {
        // 1. Открываем витрину
        driver.get(SITE_URL);
        loginIfRequired();

        // 2. Добавляем в корзину
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-card")));
        WebElement addToCartBtn = driver.findElement(By.cssSelector(".product-card button[data-action='add-to-cart']"));
        addToCartBtn.click();

        // 3. Обновляем страницу (F5)
        driver.navigate().refresh();
        loginIfRequired(); // На всякий случай, если сессия слетела

        // 4. Открываем корзину
        WebElement openCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("open-cart-btn")));
        openCartBtn.click();

        // 5. Проверяем, что корзина не пуста
        WebElement emptyMsg = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("empty-cart")));
        assertFalse(emptyMsg.isDisplayed(), "Корзина пуста после обновления страницы! Товар не сохранился.");
    }
}