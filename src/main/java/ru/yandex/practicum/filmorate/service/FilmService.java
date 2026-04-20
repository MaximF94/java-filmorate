package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilm(Long id) {
        return filmStorage.findById(id).orElseThrow(() -> {
            log.error("ID фильма не найден: {}", id);
            return new NotFoundException("Фильм с id = " + id + " не найден.");
        });
    }

    private User findUser(Long id) {
        return userStorage.findById(id).orElseThrow(() -> {
            log.error("ID пользователя не найден: {}", id);
            return new NotFoundException("Пользователь не найден");
        });
    }

    public Film create(Film film) {

        Set<String> errors = validate(film);

        if (!errors.isEmpty()) {
            log.error("Валидация фильма не пройдена: {}", film);
            throw new ValidationException(String.join(", ", errors));
        }

        filmStorage.save(film);
        log.info("Фильм успешно добавлен: {}", film.getId());
        return film;

    }

    public Film update(Film newFilm) {

        if (newFilm.getId() == null) {
            log.error("ID не указан: {}", newFilm);
            throw new ValidationException("Id должен быть указан");
        }

        Film oldFilm = filmStorage.findById(newFilm.getId())
                .orElseThrow(() -> {
                    log.error("ID не найден: {}", newFilm.getId());
                    return new NotFoundException("Фильм не найден");
                });


        updateFilmFields(oldFilm, newFilm);
        log.info("Фильм успешно обновлен: {}", newFilm.getId());
        return filmStorage.save(oldFilm);
    }

    public boolean delete(Long id) {
        log.info("Фильм успешно удален: {}", id);
        return filmStorage.delete(id);
    }

    private void updateFilmFields(Film oldFilm, Film newFilm) {
        if (!newFilm.getName().isBlank()) {
            oldFilm.setName(newFilm.getName());
        }

        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (!newFilm.getDescription().isBlank()) {
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getDuration() != null) {
            oldFilm.setDuration(newFilm.getDuration());
        }
    }

    public void addLike(Long filmId, Long userId) {

        Film film = findFilm(filmId);
        User user = findUser(userId);

        filmStorage.addLike(film, user);
        log.info("Пользователь {} поставил лайк фильму {} ", user.getId(), film.getId());

    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);
        User user = findUser(userId);

        boolean wereLike = film.getLikes().contains(user.getId());

        if (wereLike) {
            filmStorage.deleteLike(film, user);
            log.info("Пользователь {} удалил лайк фильма {} ", user.getId(), film.getId());
        } else {
            log.warn("Пользователь {} не ставил лайк фильму {}",
                    user.getId(), film.getId());
        }
    }

    public Collection<Film> getTopFilmsByLikes(Integer count) {

        Collection<Film> allFilms = filmStorage.findAll();

        Collection<Film> sortedFilms = allFilms.stream()
                .sorted(Comparator.comparingInt(this::getLikesCount).reversed())
                .toList();

        log.info("Фильмы выведены по запросу {}", count);

        return sortedFilms.stream().limit(count).collect(Collectors.toList());

    }

    private Integer getLikesCount(Film film) {
        return film.getLikes() != null ? film.getLikes().size() : 0;
    }

    Set<String> validate(Film film) {

        Set<String> errors = new HashSet<>();

        if (film.getName().isBlank()) {
            errors.add("Название не может быть пустым");
        }

        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            errors.add("Описание не должно превышать 200 символов");
        }

        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            errors.add("Дата релиза не должна быть раньше, чем 28.12.1895");
        }

        if (film.getDuration() < 0) {
            errors.add("Продолжительность фильма должна быть положительным числом");
        }

        return errors;
    }


}
