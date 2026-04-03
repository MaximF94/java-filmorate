package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();
    private Set<String> errors = new HashSet<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {

        if (!validate(user).isEmpty()) {
            logger.error("Валидация пользователя не пройдена: {}", user);
            throw new ValidationException(String.join(", ", validate(user)));
        }

        user.setId(getNextId());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);

        logger.info("Пользователь успешно добавлен: {}", user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            logger.error("ID не указан: {}", newUser);
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {

            if (!validate(newUser).isEmpty()) {
                logger.error("Валидация пользователя не пройдена: {}", newUser);
                throw new ValidationException(String.join(", ", validate(newUser)));
            }


            User oldUser = users.get(newUser.getId());

            updateUserFields(oldUser, newUser);

            logger.info("Пользователь успешно обновлен: {}", oldUser.getId());

            return oldUser;

        }
        logger.error("ID не найден: {}", newUser);
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private void updateUserFields(User oldUser, User newUser) {

        if (!newUser.getEmail().isBlank()) {
            oldUser.setEmail(newUser.getEmail());
        }

        if (!newUser.getEmail().isBlank()) {
            oldUser.setEmail(newUser.getEmail());
        }

        if (!newUser.getLogin().isBlank()) {
            oldUser.setLogin(newUser.getLogin());
        }

        if (!newUser.getName().isBlank()) {
            oldUser.setName(newUser.getName());
        }

        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
    }

    private Set<String> validate(User user) {

        errors.clear();

        if (!isValidEmail(user.getEmail())) {
            errors.add("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errors.add("логин не может быть пустым и содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            errors.add("дата рождения не может быть больше текущей даты");
        }

        return errors;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@");
    }


    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
