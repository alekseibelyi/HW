import com.codeborne.selenide.Condition;
import static com.codeborne.selenide.Selenide.$;

public class PageAssert {

    // === Проверки для формы авторизации ===

    public static void assertLoginPageIsVisible() {
        $(LoginPage.USERNAME_INPUT).shouldBe(Condition.visible);
        $(LoginPage.PASSWORD_INPUT).shouldBe(Condition.visible);
        $(LoginPage.SUBMIT_BUTTON).shouldBe(Condition.visible);
    }

    public static void assertUsernameHasText(String expectedText) {
        $(LoginPage.USERNAME_INPUT).shouldHave(Condition.value(expectedText));
    }

    public static void assertPasswordHasText(String expectedText) {
        $(LoginPage.PASSWORD_INPUT).shouldHave(Condition.value(expectedText));
    }

    // === Проверки для главной страницы ===

    public static void assertProductIsVisible(String productName) {
        new MainPage().productTitles().findBy(Condition.text(productName)).shouldBe(Condition.visible);
    }

    public static void assertCartCountIs(int expectedCount) {
        new MainPage().cartCounter().shouldHave(Condition.text(String.valueOf(expectedCount)));
    }

    public static void assertCartIsEmpty() {
        $("#empty-cart").shouldBe(Condition.visible);
    }
    // Проверка Toast-уведомления (в админке)
    public static void assertToastContains(String expectedText) {
        $(".toast").shouldBe(Condition.visible).shouldHave(Condition.text(expectedText));
    }

    // Проверка успешного оформления заказа (на главной)
    public static void assertOrderSuccess() {
        $("#main-title").shouldHave(Condition.text("Заказ успешно оформлен!"));
    }

    // Проверка итоговой суммы в корзине
    public static void assertTotalPrice(String expectedSum) {
        $("#total-price").shouldHave(Condition.text(expectedSum));
    }

    // Проверка, что в корзине отображается нужное количество товаров
    public static void assertCartHasItems(int count) {
        com.codeborne.selenide.Selenide.$$(".cart-item").shouldHave(com.codeborne.selenide.CollectionCondition.size(count));
    }
}