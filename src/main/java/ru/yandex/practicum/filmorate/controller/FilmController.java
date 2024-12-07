package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable @Positive int id) {
        log.info("Получение фильма с ID: {}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Создание нового фильма: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable @Positive int id, @PathVariable @Positive int userId) {
        log.info("Добавление лайка для фильма с ID: {} от пользователя с ID: {}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable @Positive int id, @PathVariable @Positive int userId) {
        log.info("Удаление лайка для фильма с ID: {} от пользователя с ID: {}", id, userId);
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        log.info("Получение топ {} популярных фильмов", count);
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmService.getAllFilms();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable @Positive int id) {
        filmService.deleteFilm(id);
        log.info("Фильм с ID {} был удален", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Фильм удален");
    }

    @ExceptionHandler(FilmNotFoundException.class)
    public ResponseEntity<String> handleFilmNotFoundException(FilmNotFoundException ex) {
        log.error("Фильм не найден: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Ошибка: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка на сервере");
    }
}
