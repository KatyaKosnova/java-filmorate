package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // Добавить пользователя
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    // Обновить пользователя
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    // Удалить пользователя (если необходимо)
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    // Получить список всех пользователей
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    // Добавить друга
    public void addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);  // добавляем friendId в список друзей пользователя
        friend.addFriend(userId);  // добавляем userId в список друзей друга
    }

    // Удалить друга
    public void removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.removeFriend(friendId);  // удаляем friendId из списка друзей пользователя
        friend.removeFriend(userId);  // удаляем userId из списка друзей друга
    }

    // Получить список друзей пользователя в виде List<User>
    public List<User> getFriends(int userId) {
        User user = getUserById(userId);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : user.getFriends()) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    // Получить общих друзей
    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> friends = getFriends(userId);
        List<User> otherFriends = getFriends(otherId);

        // Находим пересечение списков друзей
        friends.retainAll(otherFriends);
        return friends;
    }

    // Получить пользователя по ID
    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден.");
        }
        return user;
    }
}
