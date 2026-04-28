package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film save(Film film) {

        if (film.getId() == null) {
            film.setId(getNextId());
        }

        films.put(film.getId(), film);
        return film;

    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
    }


    @Override
    public boolean delete(Long id) {
        return films.remove(id) != null;
    }


    @Override
    public void addLike(Film film, User user) {

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        film.getLikes().add(user.getId());

    }

    @Override
    public void deleteLike(Film film, User user) {

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        film.getLikes().remove(user.getId());

    }

    @Override
    public Collection<Film> getTopFilmsByLikes(Integer count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(this::getLikesCount).reversed())
                .limit(count)
                .toList();
    }

    private Integer getLikesCount(Film film) {
        return film.getLikes() != null ? film.getLikes().size() : 0;
    }

    private long getNextId() {
        return nextId++;
    }
}
