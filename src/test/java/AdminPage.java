import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class AdminPage {

    // Форма добавления/редактирования
    public SelenideElement nameInput() { return $("#n-name"); }
    public SelenideElement priceInput() { return $("#n-price"); }
    public SelenideElement addBtn() { return $("#add-btn"); }

    // Таблица товаров
    public SelenideElement table() { return $("table"); }
    public SelenideElement tbody() { return $("tbody"); }

    // Методы взаимодействия с формой добавления
    @Step("Заполнить данные товара: имя '{name}', цена '{price}'")
    public void fillProductData(String name, String price) {
        nameInput().setValue(name);
        priceInput().setValue(price);
    }

    @Step("Нажать кнопку 'Создать'")
    public void clickAdd() {
        addBtn().click();
    }

    // --- Методы для редактирования в таблице (строка) ---

    @Step("Найти поле 'Название' для товара с ID {id}")
    public SelenideElement getNameInputById(int id) {
        return $(By.xpath("//tr[td[1]='" + id + "']//input[@type='text']"));
    }

    @Step("Найти поле 'Цена' для товара с ID {id}")
    public SelenideElement getPriceInputById(int id) {
        return $(By.xpath("//tr[td[1]='" + id + "']//input[@type='number']"));
    }

    @Step("Нажать 'Сохранить' в строке товара с ID {id}")
    public void clickSaveInRowById(int id) {
        $(By.xpath("//tr[td[1]='" + id + "']//button[contains(@class, 'btn-upd')]")).click();
    }

    @Step("Отредактировать товар в таблице (ID: {id}, новое имя: '{newName}', цена: {newPrice})")
    public void editProductInTable(int id, String newName, String newPrice) {
        getNameInputById(id).setValue(newName);
        getPriceInputById(id).setValue(newPrice);
        clickSaveInRowById(id);
    }
}