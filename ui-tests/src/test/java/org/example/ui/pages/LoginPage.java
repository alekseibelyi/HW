package org.example.ui.pages;

import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public static final String USERNAME_INPUT = "#username";
    public static final String PASSWORD_INPUT = "#password";
    public static final String SUBMIT_BUTTON = "button[type='submit']";

    @Step("Ввести логин '{username}'")
    public LoginPage enterUsername(String username) {
        $(USERNAME_INPUT).setValue(username);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage enterPassword(String password) {
        $(PASSWORD_INPUT).setValue(password);
        return this;
    }

    @Step("Нажать кнопку 'Войти'")
    public void clickSubmit() {
        $(SUBMIT_BUTTON).click();
    }

    @Step("Авторизоваться с логином '{username}'")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }
}