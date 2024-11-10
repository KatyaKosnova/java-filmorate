package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.yandex.practicum.filmorate.controller.UserValidator;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Validator validator = new UserValidator(); // Ваш валидатор

    @Test
    void testUserValidation_ValidUser() {
        User user = new User();
        user.setEmail("valid.email@example.com");
        user.setLogin("validlogin");
        user.setName("Valid User");
        user.setBirthday(LocalDate.parse("1990-01-01")); // Используем LocalDate

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);
        assertFalse(errors.hasErrors(), "Should not have validation errors");
    }

    @Test
    void testUserValidation_InvalidUser() {
        User user = new User();
        user.setEmail("invalid.email"); // Неверный email
        user.setLogin("invalid login"); // Логин с пробелами
        user.setName(""); // Имя пустое
        user.setBirthday(LocalDate.parse("2025-01-01")); // Дата рождения в будущем

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);

        assertTrue(errors.hasErrors(), "Should have validation errors");
    }
}
