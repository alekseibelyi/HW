import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class AdminPage {

    // Форма добавления/редактирования
    public SelenideElement nameInput() { return $("#n-name"); }
    public SelenideElement priceInput() { return $("#n-price"); }
    public SelenideElement addBtn() { return $("#add-btn"); }

    // Таблица товаров
    public SelenideElement table() { return $("table"); }
    public SelenideElement tbody() { return $("tbody"); }

    // Методы взаимодействия
    public void fillProductData(String name, String price) {
        nameInput().setValue(name);
        priceInput().setValue(price);
    }

    public void clickAdd() {
        addBtn().click();
    }

    // Найти строку по ID и нажать "Редактировать"
    public void clickEditById(int id) {
        String xpathRow = String.format("//tr[td[1][normalize-space()='%d']]", id);
        $(org.openqa.selenium.By.xpath(xpathRow)).$(".btn-upd").click();
    }

    // Найти строку по ID и нажать "Сохранить"
    public void clickSaveById(int id) {
        String xpathRow = String.format("//tr[td[1][normalize-space()='%d']]", id);
        $(org.openqa.selenium.By.xpath(xpathRow)).$x(".//button[contains(text(), 'Сохранить')]").click();
    }
}