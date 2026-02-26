package ui;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ui.pages.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

@DisplayName("Тесты входа в систему")
public class LoginTest extends BaseUITest {

    private String testEmail;
    private String testPassword;
    private String testName;

    @Before
    public void setUp() {
        super.setUp();

        testEmail = generateRandomEmail();
        testPassword = "1234567";
        testName = generateRandomName();

        // Регистрируем пользователя через API или UI
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open();
        registerPage.register(testName, testEmail, testPassword);

        // Ждем результат регистрации
        boolean registrationSuccess = registerPage.waitForSuccessfulRegistration();

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

        // Используем метод из Page Object вместо прямого WebDriverWait
        assertTrue("Пользователь должен быть авторизован",
                mainPage.waitForPlaceOrderButton());
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

        assertTrue("Пользователь должен быть авторизован",
                mainPage.waitForPlaceOrderButton());
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    @Description("Проверка входа через ссылку 'Войти' на странице регистрации")
    public void testLoginViaRegisterForm() {
        RegisterPage registerPage = new RegisterPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        registerPage.open();
        registerPage.clickLoginLink();
        loginPage.waitForPageLoad(); // Ждем загрузки страницы логина
        loginPage.login(testEmail, testPassword);

        MainPage mainPage = new MainPage(driver);
        assertTrue("Пользователь должен быть авторизован",
                mainPage.waitForPlaceOrderButton());
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    @Description("Проверка входа через ссылку 'Войти' на странице восстановления пароля")
    public void testLoginViaForgotPasswordForm() {
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        forgotPasswordPage.open();
        forgotPasswordPage.clickLoginLink();
        loginPage.waitForPageLoad(); // Ждем загрузки страницы логина
        loginPage.login(testEmail, testPassword);

        MainPage mainPage = new MainPage(driver);
        assertTrue("Пользователь должен быть авторизован",
                mainPage.waitForPlaceOrderButton());
    }
}