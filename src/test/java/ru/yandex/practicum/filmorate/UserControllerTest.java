package ru.yandex.practicum.filmorate;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindAllUsers() throws Exception {
        // Подготовка данных
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 1, 1));

        when(userService.getUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].name").value("User One"))
                .andExpect(jsonPath("$[1].name").value("User Two"));

        verify(userService, times(1)).getUsers();
    }

    @Test
    public void testGetUserById() throws Exception {
        // Подготовка данных
        User user = new User();
        user.setId(1L);
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User One"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testCreateUser() throws Exception {
        // Подготовка данных
        User user = new User();
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1)); // Date in LocalDate format

        when(userService.addUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user1@example.com\",\"login\":\"user1\",\"name\":\"User One\",\"birthday\":\"1990-01-01\"}"))
                .andExpect(status().isCreated()) // Ожидаем статус 201
                .andExpect(jsonPath("$.name").value("User One"));

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Строка с данными пользователя в формате JSON
        String userJson = "{\"id\":1,\"email\":\"user1@example.com\",\"login\":\"user1\",\"name\":\"User One\",\"birthday\":\"1990-01-01\"}";

        // Создание mock-объекта для сервиса
        User user = new User();
        user.setId(1L);
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        when(userService.updateUser(any(User.class))).thenReturn(user);

        // Выполнение PUT-запроса
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)) // Передаем строку JSON
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(jsonPath("$.name").value("User One"));

        // Проверяем, что метод updateUser был вызван один раз
        verify(userService, times(1)).updateUser(any(User.class));
    }


    @Test
    public void testAddFriend() throws Exception {
        mockMvc.perform(put("/users/{id}/friends/{friendId}", 1L, 2L))
                .andExpect(status().isOk());

        verify(userService, times(1)).addFriend(1L, 2L);
    }

    @Test
    public void testDeleteFriend() throws Exception {
        mockMvc.perform(delete("/users/{id}/friends/{friendId}", 1L, 2L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteFriend(1L, 2L);
    }

    @Test
    public void testFindFriends() throws Exception {
        // Подготовка данных
        User friend = new User();
        friend.setId(2L);
        friend.setEmail("friend@example.com");
        friend.setLogin("friend");
        friend.setName("Friend User");
        friend.setBirthday(LocalDate.of(1992, 1, 1));

        when(userService.findFriends(1L)).thenReturn(Collections.singletonList(friend));

        mockMvc.perform(get("/users/{id}/friends", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Friend User"));

        verify(userService, times(1)).findFriends(1L);
    }

    @Test
    public void testSharedFriends() throws Exception {
        // Подготовка данных
        User friend = new User();
        friend.setId(2L);
        friend.setEmail("friend@example.com");
        friend.setLogin("friend");
        friend.setName("Friend User");
        friend.setBirthday(LocalDate.of(1992, 1, 1));

        when(userService.findSharedFriends(1L, 2L)).thenReturn(Collections.singletonList(friend));

        mockMvc.perform(get("/users/{id}/friends/common/{otherId}", 1L, 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Friend User"));

        verify(userService, times(1)).findSharedFriends(1L, 2L);
    }


    @Test
    public void testUpdateUserSuccess() throws Exception {
        // Prepare valid user data
        String userJson = "{\"id\":1,\"email\":\"user1@example.com\",\"login\":\"user1\",\"name\":\"User One\",\"birthday\":\"1990-01-01\"}";

        User user = new User();
        user.setId(1L);
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk()) // Expect 200 status
                .andExpect(jsonPath("$.name").value("User One"));

        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    @DisplayName("Test user creation with 'birthday' field")
    public void testCreateUserBirthdayField() throws Exception {
        User user = new User();
        user.setEmail("user2@example.com");
        user.setLogin("user2");
        user.setName("User Two");
        user.setBirthday(LocalDate.of(1992, 5, 15));

        String userJson = "{\n" +
                "\"email\": \"user2@example.com\",\n" +
                "\"login\": \"user2\",\n" +
                "\"name\": \"User Two\",\n" +
                "\"birthday\": \"1992-05-15\"\n" +
                "}";

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        assertThat(responseContent).contains("\"birthday\":\"1992-05-15\"");
    }
}
