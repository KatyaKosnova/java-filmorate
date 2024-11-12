package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    // Используем FilmValidator
    private final FilmValidator filmValidator = new FilmValidator();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлён фильм: {}", film);
        } else {
            log.error("Фильм с ID {} не найден для обновления.", film.getId());
            throw new ValidationException("Фильм с таким ID не найден.");
        }
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void validateFilm(Film film) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(film, "film");
        ValidationUtils.invokeValidator(filmValidator, film, errors);

        if (errors.hasErrors()) {
            throw new ValidationException("Ошибка валидации фильма: " + errors.getAllErrors());
        }
    }
}
