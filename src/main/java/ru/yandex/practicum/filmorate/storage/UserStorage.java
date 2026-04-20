package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    public User save(User user);

    public Collection<User> findAll();

    public Optional<User> findById(Long id);

    public boolean delete(Long id);

    public void addFriend(User user, User friend);

    public Set<User> getFriends(User user);

    public void deleteFriend(User user, User friend);

    public Set<User> getCommonFriends(User user, User other);
}
