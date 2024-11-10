package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * User.
 */
@Getter
@Setter
public class User {
    private int id;                // целочисленный идентификатор
    private String email;          // электронная почта
    private String login;          // логин пользователя
    private String name;           // имя для отображения
    private LocalDate birthday;    // дата рождения
}