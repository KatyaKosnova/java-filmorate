package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        log.info("New film added: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws FilmNotFoundException {
        Long id = film.getId();
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Attempt to update film with absent id = %d", id));
        }
        films.put(id, film);
        log.info("Film {} has been successfully updated", film);
        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) throws FilmNotFoundException {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Request film by id when id is absent, id = %d", id));
        }
        return films.get(id);
    }

    @Override
    public Film deleteFilm(Film film) throws FilmNotFoundException {
        // Проверяем, существует ли фильм в хранилище
        if (films.containsKey(film.getId())) {
            // Реализуем удаление фильма
            Film removedFilm = films.remove(film.getId());

            // Если фильм был удален, логируем это
            log.info("Film {} was deleted", removedFilm);

            return removedFilm;
        } else {
            // Если фильм не найден, выбрасываем исключение
            throw new FilmNotFoundException(String.format("Attempt to delete film with absent id = %d", film.getId()));
        }
    }

    @Override
    public List<Film> getFilmsByRating(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getRating(), f1.getRating())) // Сортировка по рейтингу
                .limit(count) // Ограничиваем количество
                .collect(Collectors.toList());
    }
}