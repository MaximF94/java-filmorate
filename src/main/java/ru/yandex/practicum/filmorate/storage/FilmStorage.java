package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    public Film save(Film film);

    public boolean delete(Long id);

    public Collection<Film> findAll();

    public Optional<Film> findById(Long id);

    public void addLike(Film film, User user);

    public void deleteLike(Film film, User user);

    public Collection<Film> getTopFilmsByLikes(Integer count);
}
