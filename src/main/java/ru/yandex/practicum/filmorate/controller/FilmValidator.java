package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator implements Validator {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean supports(Class<?> clazz) {
        return Film.class.equals(clazz); // Указываем, что валидатор работает с классом Film
    }

    @Override
    public void validate(Object target, Errors errors) {
        Film film = (Film) target;

        if (film.getName() == null || film.getName().isBlank()) {
            errors.rejectValue("name", "field.required", "Название фильма не может быть пустым.");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            errors.rejectValue("description", "field.length", "Описание не должно превышать 200 символов.");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            errors.rejectValue("releaseDate", "field.invalid", "Дата релиза не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            errors.rejectValue("duration", "field.invalid", "Продолжительность фильма должна быть положительным числом.");
        }
    }
}
