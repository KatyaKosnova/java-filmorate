package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film) throws FilmNotFoundException;


    Collection<Film> getFilms();

    Film getFilmById(Long id) throws FilmNotFoundException;

    Film deleteFilm(Film film) throws FilmNotFoundException;


    List<Film> getFilmsByRating(int count);
}