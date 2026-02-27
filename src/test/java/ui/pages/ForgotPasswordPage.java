package ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ForgotPasswordPage extends BasePage {

    private static final String URL = "https://stellarburgers.education-services.ru/forgot-password";

    // Локаторы - добавили static final
    private static final By LOGIN_LINK = By.xpath("//a[text()='Войти']");

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу восстановления пароля")
    public void open() {
        driver.get(URL);
    }

    @Step("Клик по ссылке 'Войти'")
    public void clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_LINK)).click();
    }
}