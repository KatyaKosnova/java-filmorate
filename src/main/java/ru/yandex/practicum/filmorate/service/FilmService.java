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

    // Добавление нового фильма
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    // Обновление информации о фильме
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    // Получение фильма по ID
    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с ID " + id + " не найден."));
    }

    // Добавление лайка фильму
    public Film addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.addLike(userId);
        return film;
    }

    // Удаление лайка у фильма
    public Film removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.removeLike(userId);
        return film;
    }

    // Получение списка самых популярных фильмов
    public List<Film> getMostPopularFilms(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество фильмов должно быть больше 0.");
        }
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    // Удаление фильма по ID
    public void deleteFilm(int id) {
        Film film = getFilmById(id);
        filmStorage.deleteFilm(id);
    }

    // Получение всех фильмов
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }
}
