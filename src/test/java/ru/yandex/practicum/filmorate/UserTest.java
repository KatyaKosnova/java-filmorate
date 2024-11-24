package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import ru.yandex.practicum.filmorate.controller.UserValidator;
import ru.yandex.practicum.filmorate.model.User;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class UserTest {

    private UserValidator userValidator;
    private Errors errors;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
        errors = new MapBindingResult(new java.util.HashMap<>(), "user");
    }

    @Test
    void validate_ValidUser_NoErrors() {
        User validUser = new User();
        validUser.setEmail("valid@example.com");
        validUser.setLogin("validLogin");
        validUser.setName("Valid User");
        validUser.setBirthday(LocalDate.of(1990, 1, 1));

        userValidator.validate(validUser, errors);

        assertFalse(errors.hasErrors(), "There should be no validation errors.");
    }

    @Test
    void validate_InvalidEmail_ErrorAdded() {
        User invalidUser = new User();
        invalidUser.setEmail("invalidemail");
        invalidUser.setLogin("validLogin");
        invalidUser.setName("Valid User");
        invalidUser.setBirthday(LocalDate.of(1990, 1, 1));

        userValidator.validate(invalidUser, errors);

        assertTrue(errors.hasFieldErrors("email"), "There should be a validation error on the email field.");
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.",
                errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    void validate_InvalidLoginWithSpaces_ErrorAdded() {
        User invalidUser = new User();
        invalidUser.setEmail("valid@example.com");
        invalidUser.setLogin("invalid Login");
        invalidUser.setName("Valid User");
        invalidUser.setBirthday(LocalDate.of(1990, 1, 1));

        userValidator.validate(invalidUser, errors);

        assertTrue(errors.hasFieldErrors("login"), "There should be a validation error on the login field.");
        assertEquals("Логин не может быть пустым и не должен содержать пробелы.",
                errors.getFieldError("login").getDefaultMessage());
    }

    @Test
    void validate_EmptyName_SetLoginAsName() {
        User userWithEmptyName = new User();
        userWithEmptyName.setEmail("valid@example.com");
        userWithEmptyName.setLogin("validLogin");
        userWithEmptyName.setName("");  // Empty name
        userWithEmptyName.setBirthday(LocalDate.of(1990, 1, 1));

        userValidator.validate(userWithEmptyName, errors);

        assertFalse(errors.hasErrors(), "There should be no validation errors.");
        assertEquals("validLogin", userWithEmptyName.getName(), "Name should be set to login when empty.");
    }

    @Test
    void validate_BirthdayInFuture_ErrorAdded() {
        User userWithFutureBirthday = new User();
        userWithFutureBirthday.setEmail("valid@example.com");
        userWithFutureBirthday.setLogin("validLogin");
        userWithFutureBirthday.setName("Valid User");
        userWithFutureBirthday.setBirthday(LocalDate.now().plusDays(1)); // Future date

        userValidator.validate(userWithFutureBirthday, errors);

        assertTrue(errors.hasFieldErrors("birthday"), "There should be a validation error on the birthday field.");
        assertEquals("Дата рождения не может быть в будущем.",
                errors.getFieldError("birthday").getDefaultMessage());
    }
}
