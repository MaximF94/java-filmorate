package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidateTests {

    private static UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void validatePostTestEmailIsNull() {
        User user = new User(1L, "", "Login", "Name", LocalDate.of(2000, 1, 2));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostTestEmailNotAt() {
        User user = new User(1L, "mail.com", "Login", "Name", LocalDate.of(2000, 1, 2));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostLoginIsBlank() {
        User user = new User(1L, "test@mail.com", "", "Name", LocalDate.of(2000, 1, 2));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostLoginWithWhitespace() {
        User user = new User(1L, "test@mail.com", "test login", "Name", LocalDate.of(2000, 1, 2));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostNullName() {
        User user = new User(1L, "test@mail.com", "login", "", LocalDate.of(2000, 1, 2));

        userController.create(user);
        Collection<User> users = userController.findAll();

        assertFalse(users.isEmpty());
    }

    @Test
    void validatePostBirthdayInFuture() {
        User user = new User(1L, "test@mail.com", "test", "Name", LocalDate.of(3000, 1, 2));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePutNullId() {
        User userSuccess = new User(1L, "test@mail.com", "test", "Name", LocalDate.of(2000, 1, 2));
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
        User userSuccess = new User(1L, "test@mail.com", "test", "Name", LocalDate.of(2000, 1, 2));
        userController.create(userSuccess);

        User user = new User();
        user.setId(2L);
        user.setEmail("my@mail.com");
        user.setLogin("loginThis");
        user.setName("MyNameIs");
        user.setBirthday(LocalDate.of(1990, 1, 2));

        assertThrows(NotFoundException.class, () -> userController.update(user));
    }


}
