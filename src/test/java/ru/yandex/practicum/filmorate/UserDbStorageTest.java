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

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserRowMapper.class, UserRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userStorage;

    private User createUser() {
        User user = new User();
        user.setEmail("mytestemail@mail.ru");
        user.setLogin("test");
        user.setName("Test Testov");
        user.setBirthday(LocalDate.of(1975, 1, 1));
        user.setFriends(addFriends());
        return user;
    }

    private Set<Long> addFriends() {
        Set<Long> friends = new HashSet<>();
        friends.add(1L);
        friends.add(2L);
        friends.add(3L);
        return friends;
    }


    @Test
    void testFindUserById() {
        Optional<User> userOptional = userStorage.findById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void testCreateUser() {
        User user = createUser();

        User createdUser = userStorage.save(user);

        assertThat(createdUser).isNotNull();
    }

    @Test
    void shouldFindAllUsers() {
        Collection<User> users = userStorage.findAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    void shouldAddFriend() {

        Optional<User> user = userStorage.findById(1L);
        Optional<User> friend = userStorage.findById(2L);


        userStorage.addFriend(user.get(), friend.get());

        assertThat(user).isPresent();
        assertThat(user.get().getFriends().contains(1L));
    }


}
