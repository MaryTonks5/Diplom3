package ui;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ui.pages.LoginPage;
import ui.pages.RegisterPage;

import static org.junit.Assert.assertTrue;

@DisplayName("Тесты регистрации")
@RunWith(Parameterized.class)
public class RegistrationTest extends BaseUITest {

    private final String password;
    private final boolean shouldSucceed;

    public RegistrationTest(String password, boolean shouldSucceed) {
        this.password = password;
        this.shouldSucceed = shouldSucceed;
    }

    @Parameterized.Parameters(name = "Пароль: {0} -> Успех: {1}")
    public static Object[][] testData() {
        return new Object[][]{
                {"123456", true},  // 6 символов - успех
                {"1234567", true}, // больше 6 - успех
                {"12345", false},  // 5 символов - ошибка
                {"123", false},    // 3 символа - ошибка
                {"", false}        // пустой - ошибка
        };
    }

    @Test
    @DisplayName("Проверка регистрации с разными паролями")
    @Description("Проверяем успешную регистрацию (пароль 6+ символов) и ошибку (пароль меньше 6 символов)")
    public void testRegistrationWithDifferentPasswords() {
        RegisterPage registerPage = new RegisterPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        String name = generateRandomName();
        String email = generateRandomEmail();

        registerPage.open();
        registerPage.register(name, email, password);

        if (shouldSucceed) {
            // Проверяем, что открылась страница логина
            assertTrue("Должен быть редирект на страницу логина",
                    driver.getCurrentUrl().contains("login"));
        } else {
            // Проверяем, что отображается ошибка
            assertTrue("Должна отображаться ошибка о некорректном пароле",
                    registerPage.isErrorMessageDisplayed());
        }
    }
}