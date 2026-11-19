import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    // === Элементы ===
    public ElementsCollection productCards() { return $$(".product-card"); }
    public ElementsCollection productTitles() { return $$(".product-card h4"); }

    public SelenideElement addToCartButton(String productName) {
        return productCards().findBy(text(productName)).$("button[data-action='add-to-cart']");
    }

    public SelenideElement qtyInput(String productName) {
        return productCards().findBy(text(productName)).$(".qty-input");
    }

    public SelenideElement openCartButton() { return $("#open-cart-btn"); }
    public SelenideElement cartCounter() { return $("#cart-count"); }
    public SelenideElement makeOrderBtn() { return $("#makeOrder"); }

    // Для проверок
    public SelenideElement productsGrid() { return $(".products-grid"); }
    public SelenideElement mainTitle() { return $("#main-title"); }

    // === Методы взаимодействия ===
    @Step("Нажать 'В корзину' для товара '{productName}'")
    public void clickAddToCart(String productName) {
        addToCartButton(productName).click();
    }

    @Step("Ввести количество {qty} для товара '{productName}'")
    public void setQuantity(String productName, String qty) {
        qtyInput(productName).setValue(qty);
    }

    @Step("Открыть корзину")
    public void clickOpenCart() {
        openCartButton().click();
    }

    @Step("Нажать 'Оформить заказ'")
    public void clickMakeOrder() {
        makeOrderBtn().click();
    }
}