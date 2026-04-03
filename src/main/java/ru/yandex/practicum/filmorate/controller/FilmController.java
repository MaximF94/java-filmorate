package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);

    private final int maxLengthDescription = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();
    Set<String> errors = new HashSet<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        if (!validate(film).isEmpty()) {
            logger.error("Валидация фильма не пройдена: {}", film);
            throw new ValidationException(String.join(", ", validate(film)));
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        logger.info("Фильм успешно добавлен: {}", film.getId());
        return film;

    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {

        if (newFilm.getId() == null) {
            logger.error("ID не указан: {}", newFilm);
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            if (!validate(newFilm).isEmpty()) {
                logger.error("Валидация фильма не пройдена: {}", newFilm);
                throw new ValidationException(String.join(", ", validate(newFilm)));
            }
            Film oldFilm = films.get(newFilm.getId());

            updateFilmFields(oldFilm, newFilm);
            logger.info("Фильм успешно обновлен: {}", newFilm.getId());

            return oldFilm;
        }

        logger.error("ID не найден: {}", newFilm);
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");

    }

    Set<String> validate(Film film) {

        errors.clear();

        if (film.getName().isBlank()) {
            errors.add("Название не может быть пустым");
        }

        if (film.getDescription().length() > maxLengthDescription) {
            errors.add("Описание не должно превышать 200 символов");
        }

        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            errors.add("Дата релиза не должна быть раньше, чем 28.12.1895");
        }

        if (film.getDuration() < 0) {
            errors.add("Продолжительность фильма должна быть положительным числом");
        }

        return errors;
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

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
