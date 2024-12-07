package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с ID " + id + " не найден."));
    }

    public Film addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.addLike(userId);
        return film; // возвращаем обновленный фильм
    }

    public Film removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.removeLike(userId);
        return film; // возвращаем обновленный фильм
    }

    public List<Film> getMostPopularFilms(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество фильмов должно быть больше 0.");
        }
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
