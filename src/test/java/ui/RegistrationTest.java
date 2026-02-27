package ui;

import api.clients.UserClient;
import api.models.User;
import api.models.UserCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ui.pages.LoginPage;
import ui.pages.RegisterPage;

import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

@DisplayName("Тесты регистрации")
@RunWith(Parameterized.class)
public class RegistrationTest extends BaseUITest {

    private final String password;
    private final boolean shouldSucceed;
    private UserClient userClient;
    private String accessToken;
    private User createdUser;

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

    @After
    public void tearDown() {
        // Удаляем пользователя после теста, если он был создан
        if (accessToken != null && userClient != null) {
            userClient.deleteUser(accessToken);
            System.out.println("Пользователь удален после теста");
        }
        super.tearDown();
    }

    @Test
    @DisplayName("Проверка регистрации с разными паролями")
    @Description("Проверяем успешную регистрацию (пароль 6+ символов) и ошибку (пароль меньше 6 символов)")
    public void testRegistrationWithDifferentPasswords() {
        RegisterPage registerPage = new RegisterPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        String name = generateRandomName();
        String email = generateRandomEmail();

        // Открываем страницу регистрации
        registerPage.open();

        // Заполняем форму и отправляем
        registerPage.register(name, email, password);

        if (shouldSucceed) {
            // Проверяем, что открылась страница логина (успешная регистрация)
            boolean isLoginPageDisplayed = loginPage.waitForPageLoad();
            assertTrue("Должен быть редирект на страницу логина", isLoginPageDisplayed);

            // Проверяем через API, что пользователь действительно создан
            userClient = new UserClient();
            User tempUser = new User(email, password, name);
            ValidatableResponse loginResponse = userClient.loginUser(UserCredentials.fromUser(tempUser));

            int statusCode = loginResponse.extract().statusCode();
            if (statusCode == SC_OK) {
                accessToken = loginResponse.extract().path("accessToken");
                createdUser = tempUser;
            }

            assertEquals("Пользователь должен успешно логиниться после регистрации",
                    SC_OK, statusCode);

        } else {
            // Проверяем, что отображается ошибка
            boolean isErrorDisplayed = registerPage.isErrorMessageDisplayed();
            assertTrue("Должна отображаться ошибка о некорректном пароле",
                    isErrorDisplayed);

            // Проверяем через API, что пользователь НЕ создан
            userClient = new UserClient();
            User tempUser = new User(email, password, name);
            ValidatableResponse loginResponse = userClient.loginUser(UserCredentials.fromUser(tempUser));

            int statusCode = loginResponse.extract().statusCode();
            assertFalse("Пользователь с некорректным паролем НЕ должен создаваться",
                    statusCode == SC_OK);
        }
    }
}