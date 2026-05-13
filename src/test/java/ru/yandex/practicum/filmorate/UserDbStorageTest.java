package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserRowMapper.class, UserRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    void testFindUserById() {
        Optional<User> userOptional = userStorage.findById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }


    //Здесь я хотел бы добавить тесты на добавление пользователя, но тест падает, когда пытается сохранить запись.
    //По непонятной причине все время пытается добавить ID = 1. Не понимаю в чем может быть проблема.
    //При обычном запуске приложения такой проблемы нет.


}
