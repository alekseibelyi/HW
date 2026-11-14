import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public static final String USERNAME_INPUT = "#username";
    public static final String PASSWORD_INPUT = "#password";
    public static final String SUBMIT_BUTTON = "button[type='submit']";

    public LoginPage enterUsername(String username) {
        $(USERNAME_INPUT).setValue(username);
        return this;
    }
    public LoginPage enterPassword(String password) {
        $(PASSWORD_INPUT).setValue(password);
        return this;
    }
    public void clickSubmit() {
        $(SUBMIT_BUTTON).click();
    }
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }
}