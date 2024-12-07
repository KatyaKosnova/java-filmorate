package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Фильм.
 */
@Getter
@Setter
public class Film {
    private int id;                // целочисленный идентификатор
    private String name;           // название фильма
    private String description;    // описание фильма
    private LocalDate releaseDate; // дата релиза
    private int duration;          // продолжительность фильма в минутах
    private Set<Integer> likes = new HashSet<>(); // Список ID пользователей, которые поставили лайк

    /**
     * Добавить лайк от пользователя.
     *
     * @param userId ID пользователя.
     */
    public void addLike(int userId) {
        likes.add(userId);
    }

    /**
     * Удалить лайк от пользователя.
     *
     * @param userId ID пользователя.
     */
    public void removeLike(int userId) {
        likes.remove(userId);
    }

    /**
     * Получить количество лайков.
     *
     * @return количество лайков.
     */
    public int getLikesCount() {
        return likes.size();
    }

    // Переопределение equals и hashCode для корректной работы с коллекциями
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
