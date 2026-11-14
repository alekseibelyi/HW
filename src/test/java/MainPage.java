import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.text;

public class MainPage {

    // === 1. Коллекция карточек товаров ===
    public ElementsCollection productCards() {
        return $$(".product-card");
    }

    // === 2. Коллекция названий товаров ===
    public ElementsCollection productTitles() {
        return $$(".product-card h4");
    }

    // === 3. Кнопка "В корзину" для конкретного товара ===
    public SelenideElement addToCartButton(String productName) {
        return productCards().findBy(text(productName)).$("button[data-action='add-to-cart']");
    }

    // === 4. Поле ввода количества ===
    public SelenideElement qtyInput(String productName) {
        return productCards().findBy(text(productName)).$(".qty-input");
    }

    // === 5. Кнопка открытия корзины ===
    public SelenideElement openCartButton() {
        return $("#open-cart-btn");
    }

    // === 6. Счетчик товаров в корзине ===
    public SelenideElement cartCounter() {
        return $("#cart-count");
    }

    // === Методы взаимодействия ===

    public void clickAddToCart(String productName) {
        addToCartButton(productName).click();
    }

    public void setQuantity(String productName, String qty) {
        qtyInput(productName).setValue(qty);
    }

    public void clickOpenCart() {
        openCartButton().click();
    }
    // Кнопка "Оформить заказ" внутри корзины
    public SelenideElement makeOrderBtn() {
        return $("#makeOrder");
    }

    public void clickMakeOrder() {
        makeOrderBtn().click();
    }
}