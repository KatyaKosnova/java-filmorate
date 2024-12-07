package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;

    // Добавление нового фильма
    @Override
    public Film addFilm(Film film) {
        film.setId(idCounter++);
        films.put(film.getId(), film);
        return film;
    }

    // Обновление информации о фильме
    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }
        throw new IllegalArgumentException("Фильм с ID " + film.getId() + " не найден.");
    }

    // Удаление фильма
    @Override
    public void deleteFilm(int id) {
        films.remove(id);
    }

    // Получение списка всех фильмов
    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    // Получение фильма по ID
    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(films.get(id));
    }
}
