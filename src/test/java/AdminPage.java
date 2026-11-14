import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class AdminPage {

    // Геттеры (можно без @Step, чтобы не засорять отчет, но если хотите — добавьте)
    public SelenideElement nameInput() { return $("#n-name"); }
    public SelenideElement priceInput() { return $("#n-price"); }
    public SelenideElement addBtn() { return $("#add-btn"); }

    public SelenideElement table() { return $("table"); }
    public SelenideElement tbody() { return $("tbody"); }

    // === Методы взаимодействия (С @Step) ===

    @Step("Заполнить данные товара: имя '{name}', цена '{price}'")
    public void fillProductData(String name, String price) {
        nameInput().setValue(name);
        priceInput().setValue(price);
    }

    @Step("Нажать кнопку 'Создать'")
    public void clickAdd() {
        addBtn().click();
    }

    @Step("Нажать 'Редактировать' для товара с ID {id}")
    public void clickEditById(int id) {
        String xpathRow = String.format("//tr[td[1][normalize-space()='%d']]", id);
        $(org.openqa.selenium.By.xpath(xpathRow)).$(".btn-upd").click();
    }

    @Step("Нажать 'Сохранить' для товара с ID {id}")
    public void clickSaveById(int id) {
        String xpathRow = String.format("//tr[td[1][normalize-space()='%d']]", id);
        $(org.openqa.selenium.By.xpath(xpathRow)).$x(".//button[contains(text(), 'Сохранить')]").click();
    }
}