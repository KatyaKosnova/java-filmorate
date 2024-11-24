package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;
import ru.yandex.practicum.filmorate.controller.FilmValidator;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private final Validator validator = new FilmValidator(); // Ваш валидатор

    @Test
    void testFilmValidation_ValidFilm() {
        Film film = new Film();
        film.setName("Valid Movie");
        film.setDescription("Valid description of a movie.");
        film.setReleaseDate(LocalDate.of(1999, 1, 1)); // Дата релиза, которая подходит
        film.setDuration(120); // Положительная продолжительность

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(film, "film");
        ValidationUtils.invokeValidator(validator, film, errors);
        assertFalse(errors.hasErrors(), "Should not have validation errors");
    }

    @Test
    void testFilmValidation_InvalidFilm() {
        Film film = new Film();
        film.setName(""); // Пустое название
        film.setDescription("Short description"); // Длинное описание
        film.setReleaseDate(LocalDate.of(1800, 1, 1)); // Неверная дата релиза
        film.setDuration(-1); // Неверная продолжительность

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(film, "film");
        ValidationUtils.invokeValidator(validator, film, errors);

        assertTrue(errors.hasErrors(), "Should have validation errors");
    }
}
