package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получение пользователя с ID: {}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Добавление друга: {} к {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Удаление друга: {} у {}", friendId, id);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Получение списка друзей пользователя с ID: {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получение общих друзей пользователей с ID: {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
