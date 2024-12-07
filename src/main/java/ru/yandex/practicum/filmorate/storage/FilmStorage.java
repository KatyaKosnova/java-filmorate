package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    // Добавление нового фильма
    Film addFilm(Film film);

    // Обновление информации о фильме
    Film updateFilm(Film film);

    // Удаление фильма по ID
    void deleteFilm(int id);

    // Получение всех фильмов
    List<Film> getAllFilms();

    // Получение фильма по ID
    Optional<Film> getFilmById(int id);
}
