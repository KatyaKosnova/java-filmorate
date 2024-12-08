package ru.yandex.practicum.filmorate.controller;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator implements Validator {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1900, 1, 1);

    @Override
    public boolean supports(Class<?> clazz) {
        return Film.class.equals(clazz); // Указываем, что валидатор работает с классом Film
    }

    @Override
    public void validate(Object target, Errors errors) {
        Film film = (Film) target;

        // Проверка на пустое имя
        if (!StringUtils.hasText(film.getName())) {
            errors.rejectValue("name", "field.required", "Название фильма не может быть пустым.");
        }

        // Проверка длины описания
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            errors.rejectValue("description", "field.length", "Описание не должно превышать 200 символов.");
        }

        // Проверка на дату релиза не раньше 1900 года
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            errors.rejectValue("releaseDate", "field.invalid", "Дата релиза не может быть раньше 1900 года.");
        }

        // Проверка продолжительности
        if (film.getDuration() <= 0) {
            errors.rejectValue("duration", "field.invalid", "Продолжительность фильма должна быть положительным числом.");
        }
    }
}
