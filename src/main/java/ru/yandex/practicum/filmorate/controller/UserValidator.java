package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.Errors;
import ru.yandex.practicum.filmorate.model.GenericValidator;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator implements GenericValidator<User> {

    @Override
    public void validate(User user, Errors errors) {
        // Валидация email
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            errors.rejectValue("email", "invalid.email", "Электронная почта не может быть пустой и должна содержать символ '@'.");
        }

        // Валидация логина
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errors.rejectValue("login", "invalid.login", "Логин не может быть пустым и не должен содержать пробелы.");
        }

        // Валидация имени
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        // Валидация даты рождения
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            errors.rejectValue("birthday", "invalid.birthday", "Дата рождения не может быть в будущем.");
        }
    }
}
