package ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends BasePage {

    // Локаторы
    private final By PERSONAL_ACCOUNT_BUTTON = By.xpath("//p[text()='Личный Кабинет']");
    private final By CONSTRUCTOR_BUTTON = By.xpath("//p[text()='Конструктор']");
    private final By LOGO = By.className("AppHeader_header__logo__2D0X2");

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    @Step("Клик по кнопке 'Личный Кабинет'")
    public void clickPersonalAccount() {
        driver.findElement(PERSONAL_ACCOUNT_BUTTON).click();
    }

    @Step("Клик по кнопке 'Конструктор'")
    public void clickConstructor() {
        driver.findElement(CONSTRUCTOR_BUTTON).click();
    }

    @Step("Клик по логотипу")
    public void clickLogo() {
        driver.findElement(LOGO).click();
    }
}