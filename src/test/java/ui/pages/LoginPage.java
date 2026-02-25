package ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String URL = "https://stellarburgers.education-services.ru/login";

    // Локаторы
    private final By EMAIL_INPUT = By.xpath("//label[text()='Email']/following-sibling::input");
    private final By PASSWORD_INPUT = By.xpath("//input[@type='password']");
    private final By LOGIN_BUTTON = By.xpath("//button[text()='Войти']");
    private final By REGISTER_LINK = By.xpath("//a[text()='Зарегистрироваться']");
    private final By FORGOT_PASSWORD_LINK = By.xpath("//a[text()='Восстановить пароль']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу логина")
    public void open() {
        driver.get(URL);
    }

    @Step("Ввести email: {email}")
    public void setEmail(String email) {
        driver.findElement(EMAIL_INPUT).sendKeys(email);
    }

    @Step("Ввести пароль")
    public void setPassword(String password) {
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
    }

    @Step("Клик по кнопке 'Войти'")
    public void clickLoginButton() {
        driver.findElement(LOGIN_BUTTON).click();
    }

    @Step("Выполнить вход")
    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    @Step("Клик по ссылке 'Зарегистрироваться'")
    public void clickRegisterLink() {
        driver.findElement(REGISTER_LINK).click();
    }

    @Step("Клик по ссылке 'Восстановить пароль'")
    public void clickForgotPasswordLink() {
        driver.findElement(FORGOT_PASSWORD_LINK).click();
    }
}