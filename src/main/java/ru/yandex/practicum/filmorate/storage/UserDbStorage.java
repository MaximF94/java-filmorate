package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final UserRepository userRepository;

    public UserDbStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User save(User user) {
        if (user.getId() == null) {
            return userRepository.save(user);
        }
        return userRepository.update(user);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        return userRepository.delete(id);
    }

    @Override
    public void addFriend(User user, User friend) {
        userRepository.addFriend(user, friend);
    }

    @Override
    public Set<User> getFriends(User user) {
        return userRepository.getFriends(user)
                .stream()
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        userRepository.deleteFriend(user, friend);
    }

    @Override
    public Set<User> getCommonFriends(User user, User other) {
        return userRepository.getCommonFriends(user, other)
                .stream()
                .collect(Collectors.toSet());
    }
}
