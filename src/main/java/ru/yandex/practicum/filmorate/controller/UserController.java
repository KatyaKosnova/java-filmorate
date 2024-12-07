package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Создание пользователя
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Создание пользователя: {}", user);

        // Validate login: Ensure no spaces or invalid characters
        if (user.getLogin().contains(" ") || !user.getLogin().matches("^[a-zA-Z0-9_]+$")) {
            log.error("Неверный формат логина: {}", user.getLogin());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Validate email: Check if it has a valid format using regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!user.getEmail().matches(emailRegex)) {
            log.error("Неверный формат email: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Validate birthday: Ensure it's a valid past date
        try {
            LocalDate birthday = user.getBirthday(); // Assuming this is a LocalDate object
            if (birthday.isAfter(LocalDate.now())) {
                log.error("Дата рождения не может быть в будущем: {}", user.getBirthday());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке даты рождения: {}", user.getBirthday());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            User createdUser = userService.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Обновление пользователя
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        try {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Получение пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        log.info("Получение пользователя с ID: {}", id);
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            log.error("Пользователь с ID {} не найден", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Получение списка всех пользователей
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Получение списка всех пользователей");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Добавление друга
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Добавление друга: {} к {}", friendId, id);
        try {
            userService.addFriend(id, friendId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Ошибка добавления друга: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Удаление друга
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Удаление друга: {} у {}", friendId, id);
        try {
            userService.removeFriend(id, friendId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Ошибка удаления друга: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Получение списка друзей пользователя
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable int id) {
        log.info("Получение списка друзей пользователя с ID: {}", id);
        try {
            List<User> friends = userService.getFriends(id);
            return ResponseEntity.ok(friends);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при получении списка друзей: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Получение списка общих друзей
    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получение общих друзей пользователей с ID: {} и {}", id, otherId);
        try {
            List<User> commonFriends = userService.getCommonFriends(id, otherId);
            return ResponseEntity.ok(commonFriends);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при получении общих друзей: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
