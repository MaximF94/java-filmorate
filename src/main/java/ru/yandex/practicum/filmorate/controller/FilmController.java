package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {


    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();


    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        Set<String> errors = validate(film);

        if (!errors.isEmpty()) {
            log.error("Валидация фильма не пройдена: {}", film);
            throw new ValidationException(String.join(", ", errors));
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен: {}", film.getId());
        return film;

    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {

        if (newFilm.getId() == null) {
            log.error("ID не указан: {}", newFilm);
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            if (!validate(newFilm).isEmpty()) {
                log.error("Валидация фильма не пройдена: {}", newFilm);
                throw new ValidationException(String.join(", ", validate(newFilm)));
            }
            Film oldFilm = films.get(newFilm.getId());

            updateFilmFields(oldFilm, newFilm);
            log.info("Фильм успешно обновлен: {}", newFilm.getId());

            return oldFilm;
        }

        log.error("ID не найден: {}", newFilm);
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");

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

    //Ранее, ты оставил комментарий "чтобы не вычислять id каждый раз, можно сохранить его в поле класса
    // и инкрементировать при каждом добавлении"
    // Вопрос: Про какой класс имелось ввиду, в котором нужно сохранить его? В pojo классах, вроде как, логику лучше не хранить. Только геттеры, сеттеры, конструктор equals и hashcode
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
