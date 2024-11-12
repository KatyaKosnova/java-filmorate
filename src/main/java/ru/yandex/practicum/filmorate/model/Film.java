package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private int id;                // целочисленный идентификатор
    private String name;           // название фильма
    private String description;     // описание фильма
    private LocalDate releaseDate;  // дата релиза
    private int duration;           // продолжительность фильма в минутах
}