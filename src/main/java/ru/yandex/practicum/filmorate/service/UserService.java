package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private Long id = 0L;

    @Autowired
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public List<Long> getCommonFriends(Long userId, Long otherId) {
        return userStorage.getCommonFriends(userId, otherId);  // Вызов метода хранилища
    }

    private Long generateId() {
        return ++id;
    }

    public User addUser(User user) {
        user.setId(generateId());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User deleteUser(User user) {
        return userStorage.deleteUser(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Long id, Long friendId) {
        // Получаем пользователей по их ID
        User user = userStorage.getUserById(id);
        User userFriend = userStorage.getUserById(friendId);

        // Проверяем, существуют ли оба пользователя
        if (user == null) {
            log.error("User with id {} not found", id);
            throw new IllegalArgumentException("User not found");
        }
        if (userFriend == null) {
            log.error("Friend with id {} not found", friendId);
            throw new IllegalArgumentException("Friend not found");
        }

        // Проверяем, не являются ли они уже друзьями
        if (user.getFriends().contains(friendId)) {
            log.warn("User id = {} and friend id = {} are already friends", id, friendId);
            return;  // Можно вернуть, если уже являются друзьями
        }

        // Добавляем друга в коллекцию друзей
        user.addFriend(friendId);
        userFriend.addFriend(id);

        log.info("User id = {} added user id = {} to friends", id, friendId);
    }


    public void deleteFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User userFriend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
        log.info("User id = {} deleted from friends user id={}", id, friendId);
    }

    public Collection<User> findFriends(Long id) {
        Collection<User> friends = new ArrayList<>();
        for (Long friendId : userStorage.getUserById(id).getFriends()) {
            if (userStorage.getUserById(friendId) != null)
                friends.add(userStorage.getUserById(friendId));
        }
        return friends;
    }

    public Collection<User> findSharedFriends(Long id, Long otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);

        // Проверка на null, если хотя бы один из пользователей не найден
        if (user == null || otherUser == null) {
            throw new IllegalArgumentException("One or both users not found");
        }

        // Получаем список общих друзей с использованием Set для улучшения производительности
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)  // Проверяем, есть ли друг у другого пользователя
                .map(userStorage::getUserById)  // Преобразуем ID в объекты User
                .filter(Objects::nonNull)  // Проверяем, что объект не равен null
                .collect(Collectors.toList());
    }
}