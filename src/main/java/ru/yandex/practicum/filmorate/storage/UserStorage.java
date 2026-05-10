package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    User save(User user);

    Collection<User> findAll();

    Optional<User> findById(Long id);

    boolean delete(Long id);

    void addFriend(User user, User friend);

    Set<User> getFriends(User user);

    void deleteFriend(User user, User friend);

    Set<User> getCommonFriends(User user, User other);
}
