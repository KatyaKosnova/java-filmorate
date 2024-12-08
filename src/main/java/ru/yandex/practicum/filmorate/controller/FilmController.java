package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        return filmService.getFilms(); // Передача ответственности сервису
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) throws FilmNotFoundException {
        log.info("Request film by id = {}", id);
        return filmService.getFilmById(id);
    }


    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("Request to add film {}", film);
        Film savedFilm = filmService.addFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFilm);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmNotFoundException {
        log.info("Request to change film {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        try {
            log.info("Request from user id = {} put like to film id = {}", userId, id);
            filmService.addLike(id, userId);
            return ResponseEntity.ok("Like added successfully");
        } catch (FilmNotFoundException ex) {
            log.error("Film not found with id = {}", id, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Film not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteMapping(@PathVariable Long id, @PathVariable Long userId) {
        try {
            log.info("Request from user id = {} delete like to film id = {}", userId, id);
            filmService.removeLike(id, userId);
            return ResponseEntity.ok("Like removed successfully");
        } catch (FilmNotFoundException ex) {
            log.error("Film not found with id = {}", id, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Film not found with id: " + id);
        }
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

    @PutMapping("/films/{filmId}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        try {
            filmService.addLike(filmId, userId);  // Вызов метода добавления лайка

            // Возвращаем JSON-ответ с сообщением о том, что лайк был успешно добавлен
            Map<String, String> response = new HashMap<>();
            response.put("message", "Like added successfully");

            return ResponseEntity.ok(response);  // Возвращаем 200 OK с JSON-ответом
        } catch (ResourceNotFoundException e) {
            // Возвращаем 404, если ресурс не найден
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Возвращаем 500, если ошибка на сервере
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}

