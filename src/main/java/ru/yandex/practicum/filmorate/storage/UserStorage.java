package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    User deleteUser(User user);

    User getUserById(Long id);

    List<Long> getCommonFriends(Long userId, Long otherId);

    Collection<User> getFriendsByUserId(Long id);
}