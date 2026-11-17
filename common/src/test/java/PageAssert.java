
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PageAssert {

    // === Проверки для формы авторизации ===
    @Step("Проверить, что страница авторизации отображается")
    public static void assertLoginPageIsVisible() {
        $(LoginPage.USERNAME_INPUT).shouldBe(Condition.visible);
        $(LoginPage.PASSWORD_INPUT).shouldBe(Condition.visible);
        $(LoginPage.SUBMIT_BUTTON).shouldBe(Condition.visible);
    }
    @Step("Проверить, что поле логина содержит текст '{expectedText}'")
    public static void assertUsernameHasText(String expectedText) {
        $(LoginPage.USERNAME_INPUT).shouldHave(Condition.value(expectedText));
    }

    @Step("Проверить, что поле пароля содержит текст '{expectedText}'")
    public static void assertPasswordHasText(String expectedText) {
        $(LoginPage.PASSWORD_INPUT).shouldHave(Condition.value(expectedText));
    }

    // === Проверки для главной страницы ===
    @Step("Проверить, что товар '{productName}' виден на странице")
    public static void assertProductIsVisible(String productName) {
        new MainPage().productTitles().findBy(Condition.text(productName)).shouldBe(Condition.visible);
    }
    @Step("Проверить, что счетчик корзины равен {expectedCount}")
    public static void assertCartCountIs(int expectedCount) {
        new MainPage().cartCounter().shouldHave(Condition.text(String.valueOf(expectedCount)));
    }

    @Step("Проверить, что корзина пуста")
    public static void assertCartIsEmpty() {
        $("#empty-cart").shouldBe(Condition.visible);
    }

    @Step("Проверить, что всплывающее уведомление содержит текст '{expectedText}'")
    public static void assertToastContains(String expectedText) {
        $(".toast").shouldBe(Condition.visible).shouldHave(Condition.text(expectedText));
    }

    @Step("Проверить, что заказ успешно оформлен")
    public static void assertOrderSuccess() {
        $("#main-title").shouldHave(Condition.text("Заказ успешно оформлен!"));
    }

    @Step("Проверить, что итоговая цена равна '{expectedSum}'")
    public static void assertTotalPrice(String expectedSum) {
        $("#total-price").shouldHave(Condition.text(expectedSum));
    }

    @Step("Проверить, что в корзине отображается {count} товаров")
    public static void assertCartHasItems(int count) {
        Selenide.$$(".cart-item").shouldHave(CollectionCondition.size(count));
    }
}