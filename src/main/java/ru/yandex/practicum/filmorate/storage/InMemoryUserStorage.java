package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        log.info("New user added: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        if (!users.containsKey(id))
            throw new UserNotFoundException(String.format("Attempt to update user with " +
                    "absent id = %d", id));
        users.put(user.getId(), user);
        log.info("User {} has been successfully updated", user);
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) return users.remove(user.getId());
        else throw new UserNotFoundException(String.format("Attempt to delete user with " +
                "absent id = %d", user.getId()));
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id))
            throw new UserNotFoundException(String.format("Request user with absent id = %d", id));
        return users.get(id);
    }

    @Override
    public List<Long> getCommonFriends(Long userId, Long otherId) {
        User user1 = users.get(userId);
        User user2 = users.get(otherId);

        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("One or both users not found");
        }

        // Фильтруем общих друзей на основе идентификаторов
        return user1.getFriends().stream()  // Предполагается, что getFriends() возвращает Set<Long>
                .filter(user2.getFriends()::contains)  // Проверяем, является ли друг из user1 другом и для user2
                .collect(Collectors.toList());  // Возвращаем список ID общих друзей
    }


    public Collection<User> getFriendsByUserId(Long id) {
        User user = getUserById(id);  // Получаем пользователя по ID
        if (user == null) {
            return Collections.emptyList();  // Если пользователь не найден, возвращаем пустой список
        }

        // Преобразуем ID друзей в объекты User, используя userStorage (или аналогичный сервис)
        return user.getFriends().stream()
                .map(this::getUserById)  // Получаем User по ID
                .filter(Objects::nonNull)  // Фильтруем null значения
                .collect(Collectors.toSet());  // Возвращаем уникальные объекты User
    }
}