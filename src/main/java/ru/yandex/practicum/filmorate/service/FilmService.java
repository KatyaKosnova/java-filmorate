package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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
        if (film.getReleaseDate().isBefore(releaseDate))
            throw new ValidationException("Attempt to add film " +
                    "with releaseDate before 28-12-1895");
        film.setId(generateId());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(Long filmId, Long userId) {
        // Проверяем наличие фильма
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new ResourceNotFoundException("Film not found with id: " + filmId);  // выбрасываем исключение, если фильма нет
        }

        // Проверяем наличие пользователя
        userStorage.getUserById(userId);  // если пользователь не найден, будет выброшено исключение
        film.addLikeFromUser(userId);
    }

    public void removeLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);  // Получаем фильм
        if (!film.hasLikeFromUser(userId)) {
            throw new UserNotFoundException(String.format("User id = %d trying to delete like to film id = %d, " +
                    "which is absent", userId, id));
        }
        log.info("User id = {} deleted like to film id = {}", userId, id);
        film.removeLikeFromUser(userId);  // Удаляем лайк
    }

    public List<Film> getFilmsByRating(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getRating).reversed())  // Сортируем по рейтингу
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film createFilm(Film film) {
        // Применяем логику проверки
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

        film.setId(generateId());
        return filmStorage.addFilm(film);  // Сохраняем фильм в хранилище
    }
}