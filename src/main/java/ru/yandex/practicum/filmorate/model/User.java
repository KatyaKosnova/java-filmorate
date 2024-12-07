package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private int id;                // целочисленный идентификатор
    private String email;          // электронная почта
    private String login;          // логин пользователя
    private String name;           // имя для отображения
    private LocalDate birthday;    // дата рождения
    private Set<Integer> friends = new HashSet<>(); // Список ID друзей

    // Добавить друга
    public void addFriend(int friendId) {
        friends.add(friendId);
    }

    // Удалить друга
    public void removeFriend(int friendId) {
        friends.remove(friendId);
    }

    // Проверить, является ли пользователь другом
    public boolean isFriend(int friendId) {
        return friends.contains(friendId);
    }
}
