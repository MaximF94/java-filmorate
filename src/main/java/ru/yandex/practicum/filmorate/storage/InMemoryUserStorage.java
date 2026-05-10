package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(getNextId());
            user.setFriends(new HashSet<>());
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean delete(Long id) {
        return users.remove(id) != null;
    }

    @Override
    public void addFriend(User user, User friend) {

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());

    }

    @Override
    public Set<User> getFriends(User user) {

        return user.getFriends().stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

    }

    @Override
    public void deleteFriend(User user, User friend) {

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());

    }

    @Override
    public Set<User> getCommonFriends(User user, User other) {
        Set<Long> userFriends = user.getFriends() != null ? user.getFriends() : Collections.emptySet();
        Set<Long> otherFriends = other.getFriends() != null ? other.getFriends() : Collections.emptySet();

        Set<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);

        return commonFriends.stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private long getNextId() {
        return nextId++;
    }
}
