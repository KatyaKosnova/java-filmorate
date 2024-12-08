package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Request all users");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Request user by id = {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation failed: " + errorMessage));
        }
        log.info("Request to add user {}", user);
        User createdUser = userService.addUser(user);  // Метод должен вернуть пользователя
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation failed: " + errorMessage));
        }
        log.info("Request update user {}", user);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser); // Status 200
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Request from user id = {} add friend id = {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Request from user id = {} delete friend id = {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable Long id) {
        log.info("Request friends from user id = {}", id);
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> sharedFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Request from user id = {} to get shared friends with user id = {}", id, otherId);
        return userService.findSharedFriends(id, otherId);
    }
}