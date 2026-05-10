package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidateTests {

    private static UserController userController;
    private static UserStorage userStorage;
    private static UserService userService;

    @BeforeEach
    void beforeEach() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void validatePostTestEmailIsNull() {
        User user = new User(1L, "", "Login", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostTestEmailNotAt() {
        User user = new User(1L, "mail.com", "Login", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostLoginIsBlank() {
        User user = new User(1L, "test@mail.com", "", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostLoginWithWhitespace() {
        User user = new User(1L, "test@mail.com", "test login", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostNullName() {
        User user = new User(1L, "test@mail.com", "login",
                "", LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(user);
        Collection<User> users = userController.findAll();

        assertFalse(users.isEmpty());
    }

    @Test
    void validatePostBirthdayInFuture() {
        User user = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(3000, 1, 2), new HashSet<>());

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePutNullId() {
        User userSuccess = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userController.create(userSuccess);

        User user = new User();
        user.setEmail("my@mail.com");
        user.setLogin("loginThis");
        user.setName("MyNameIs");
        user.setBirthday(LocalDate.of(1990, 1, 2));

        assertThrows(ValidationException.class, () -> userController.update(user));
    }

    @Test
    void validatePutNotFoundId() {
        User userSuccess = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(userSuccess);

        User user = new User();
        user.setId(2L);
        user.setEmail("my@mail.com");
        user.setLogin("loginThis");
        user.setName("MyNameIs");
        user.setBirthday(LocalDate.of(1990, 1, 2));

        assertThrows(NotFoundException.class, () -> userController.update(user));
    }

    @Test
    void validatePutAddFriend() {
        User user = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(user);

        User user2 = new User(2L, "test2@mail.com", "test2", "Name2",
                LocalDate.of(1950, 1, 2), new HashSet<>());

        userController.create(user2);

        userController.addFriend(user.getId(), user2.getId());

        Set<User> friends = userService.getFriends(user.getId());

        assertFalse(friends.isEmpty());

    }

    @Test
    void validateDeleteFriend() {
        User user = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(user);

        User user2 = new User(2L, "test2@mail.com", "test2", "Name2",
                LocalDate.of(1950, 1, 2), new HashSet<>());

        userController.create(user2);

        userController.addFriend(user.getId(), user2.getId());

        userController.deleteFriend(user.getId(), user2.getId());

        Set<User> friends = userService.getFriends(user.getId());

        assertTrue(friends.isEmpty());

    }


    @Test
    void validateIsCommonFriends() {
        User user = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(user);

        User user2 = new User(2L, "test2@mail.com", "test2", "Name2",
                LocalDate.of(1950, 1, 2), new HashSet<>());

        userController.create(user2);

        User user3 = new User(3L, "test3@mail.com", "test3", "Name3",
                LocalDate.of(1970, 1, 2), new HashSet<>());

        userController.create(user3);

        userController.addFriend(user.getId(), user2.getId());

        userController.addFriend(user.getId(), user3.getId());

        Set<User> friends = userService.getCommonFriends(user2.getId(), user3.getId());

        assertFalse(friends.isEmpty());

    }


}
