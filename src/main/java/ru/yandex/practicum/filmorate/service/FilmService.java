package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Long id = 0L;
    private static final LocalDate releaseDate = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage,
                       @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private Long generateId() {
        return ++id;
    }

    public Film addFilm(Film film) {
        validateFilm(film); // Вынесем валидацию в отдельный метод для лучшей читаемости
        film.setId(generateId());
        log.info("Adding new film: {}, release date: {}, duration: {} minutes",
                film.getName(), film.getReleaseDate(), film.getDuration());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws FilmNotFoundException {
        log.info("Updating film with id {}: {}", film.getId(), film.getName());
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        log.info("Fetching all films");
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) throws FilmNotFoundException {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException("Film not found with id: " + id);
        }
        return film;
    }

    public void addLike(Long filmId, Long userId) throws FilmNotFoundException {
        Film film = getFilmById(filmId);
        userStorage.getUserById(userId); // Проверяем наличие пользователя
        log.info("User id = {} liked film id = {}", userId, filmId);
        film.addLikeFromUser(userId);
    }

    public void removeLike(Long id, Long userId) throws FilmNotFoundException {
        Film film = getFilmById(id);
        if (!film.hasLikeFromUser(userId)) {
            throw new UserNotFoundException(String.format("User id = %d trying to delete like to film id = %d, which is absent", userId, id));
        }
        log.info("User id = {} removed like from film id = {}", userId, id);
        film.removeLikeFromUser(userId);
    }

    public List<Film> getFilmsByRating(int count) {
        log.info("Fetching top {} films by rating", count);
        return filmStorage.getFilmsByRating(count);
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(generateId());
        log.info("Creating new film: {}", film.getName());
        return filmStorage.addFilm(film);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new IllegalArgumentException("Film name cannot be null or empty");
        }
        if (film.getDescription() == null || film.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Film description cannot be null or empty");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1900, 1, 1))) {
            throw new IllegalArgumentException("Film release date must be valid and not earlier than 1900");
        }
        if (film.getDuration() <= 0) {
            throw new IllegalArgumentException("Film duration must be positive");
        }
    }
}
