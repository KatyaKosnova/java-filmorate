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
        if (userStorage.getUserById(user.getId()) != null) {
            throw new IllegalArgumentException("Пользователь с таким ID уже существует.");
        }
        return userStorage.addUser(user);
    }

    // Обновить пользователя
    public User updateUser(User user) {
        User existingUser = userStorage.getUserById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден.");
        }
        return userStorage.updateUser(user);
    }

    // Удалить пользователя (если необходимо)
    public void deleteUser(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден.");
        }
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
        if (user.isFriend(friendId)) {
            throw new IllegalArgumentException("Этот пользователь уже в списке друзей.");
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    // Удалить друга
    public void removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
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
        List<User> friends = new ArrayList<>(getFriends(userId));
        List<User> otherFriends = getFriends(otherId);
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
