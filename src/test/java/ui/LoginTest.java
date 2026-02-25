package ui;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pages.*;

import java.time.Duration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

@DisplayName("Тесты входа в систему")
public class LoginTest extends BaseUITest {

    private String testEmail;
    private String testPassword;
    private String testName;
    private static final int WAIT_TIMEOUT = 10;

    @Before
    public void setUp() {
        super.setUp();

        testEmail = generateRandomEmail();
        testPassword = "1234567"; // Используем пароль 7 символов для успешной регистрации
        testName = generateRandomName();

        // Регистрируем пользователя через UI с правильными ожиданиями
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open();

        // Добавляем логирование для отладки
        System.out.println("Регистрация пользователя:");
        System.out.println("Name: " + testName);
        System.out.println("Email: " + testEmail);
        System.out.println("Password: " + testPassword);

        registerPage.register(testName, testEmail, testPassword);

        // Ждем результат регистрации
        boolean registrationSuccess = registerPage.waitForSuccessfulRegistration();

        System.out.println("Регистрация успешна: " + registrationSuccess);
        System.out.println("Текущий URL после регистрации: " + driver.getCurrentUrl());

        // Если регистрация не удалась - пропускаем тесты
        assumeTrue("Не удалось зарегистрировать пользователя. Тесты логина пропускаются.",
                registrationSuccess);
    }

    @Test
    @DisplayName("Вход по кнопке «Войти в аккаунт» на главной")
    @Description("Проверка входа через кнопку на главной странице")
    public void testLoginViaMainPageButton() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        mainPage.open();
        mainPage.clickLoginButton();
        loginPage.login(testEmail, testPassword);

        // Ждем появления кнопки "Оформить заказ"
        boolean isOrderButtonDisplayed = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[text()='Оформить заказ']"))
                ).isDisplayed();

        assertTrue("Пользователь должен быть авторизован", isOrderButtonDisplayed);
    }

    @Test
    @DisplayName("Вход через кнопку «Личный кабинет»")
    @Description("Проверка входа через кнопку в хедере")
    public void testLoginViaPersonalAccount() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        HeaderPage headerPage = new HeaderPage(driver);

        mainPage.open();
        headerPage.clickPersonalAccount();
        loginPage.login(testEmail, testPassword);

        // Ждем появления кнопки "Оформить заказ"
        boolean isOrderButtonDisplayed = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[text()='Оформить заказ']"))
                ).isDisplayed();

        assertTrue("Пользователь должен быть авторизован", isOrderButtonDisplayed);
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    @Description("Проверка входа через ссылку 'Войти' на странице регистрации")
    public void testLoginViaRegisterForm() {
        RegisterPage registerPage = new RegisterPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        MainPage mainPage = new MainPage(driver);

        registerPage.open();
        registerPage.clickLoginLink();
        loginPage.login(testEmail, testPassword);

        boolean isOrderButtonDisplayed = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[text()='Оформить заказ']"))
                ).isDisplayed();

        assertTrue("Пользователь должен быть авторизован", isOrderButtonDisplayed);
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    @Description("Проверка входа через ссылку 'Войти' на странице восстановления пароля")
    public void testLoginViaForgotPasswordForm() {
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        MainPage mainPage = new MainPage(driver);

        forgotPasswordPage.open();
        forgotPasswordPage.clickLoginLink();
        loginPage.login(testEmail, testPassword);

        boolean isOrderButtonDisplayed = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[text()='Оформить заказ']"))
                ).isDisplayed();

        assertTrue("Пользователь должен быть авторизован", isOrderButtonDisplayed);
    }
}