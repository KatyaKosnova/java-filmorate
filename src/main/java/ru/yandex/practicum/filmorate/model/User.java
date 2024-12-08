package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;

    @NotNull
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Login is required")
    @Pattern(regexp = "\\S+", message = "Login cannot contain spaces")
    private String login;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Birthday is mandatory")
    @Past(message = "Birthday cannot be in the future")
    private LocalDate birthday;

    @NotNull
    private Set<Long> friends = new HashSet<>(); // Storing only friends' IDs

    // Method to get the user's name, defaulting to login if name is blank
    public String getName() {
        return (name == null || name.isBlank()) ? login : name;
    }

    // Method to add a friend (prevents duplicates)
    public void addFriend(Long friendId) {
        if (friendId != null && !friends.contains(friendId)) {
            friends.add(friendId);
        }
    }

    // Method to remove a friend
    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }

    // Method to get the count of friends
    public int getFriendsCount() {
        return friends.size();
    }

    // Method to check if a given ID is already a friend
    public boolean isFriend(Long friendId) {
        return friends.contains(friendId);
    }
}
