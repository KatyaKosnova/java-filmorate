package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Validated
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Request all films");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("Request film by id = {}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        try {
            log.info("Request to add film {}", film);
            // Пытаемся создать фильм
            Film savedFilm = filmService.addFilm(film);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFilm);
        } catch (ResponseStatusException e) {
            // Ловим ResponseStatusException для обработки ошибок валидации
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            // Обрабатываем другие непредвиденные ошибки (например, ошибки сервера)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Request to change film {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Request from user id = {} put like to film id = {}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteMapping(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Request from user id = {} delete like to film id = {}", userId, id);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(required = false) Integer count) {
        log.info("Request best films, count = {}", count);
        if (count == null) count = 10;
        return filmService.getFilmsByRating(count);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            message.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");
        }
        return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{filmId}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        try {
            filmService.addLike(filmId, userId);
            return ResponseEntity.ok().build();
        } catch (FilmNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film not found");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
