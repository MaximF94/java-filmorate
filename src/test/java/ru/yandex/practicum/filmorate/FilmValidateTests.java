package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


public class FilmValidateTests {

    private static FilmController filmController;
    private static UserController userController;

    @BeforeEach
    void beforeEach() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void validatePostTestDescriptionMaxSymbols() {

        Film film = new Film(1L, "test film", "Java — мощный язык программирования, используемый для " +
                "создания надёжных приложений. Он поддерживает ООП, многопоточность и работает " +
                "на разных платформах. Изучать Java интересно и полезно для разработч",
                LocalDate.of(2004, 1, 1), 120L, new HashSet<>());

        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());

    }

    @Test
    void validatePostTestEarlyDateFilm() {

        Film film = new Film(1L, "first film", "description of first film",
                LocalDate.of(1895, 12, 28), 120L, new HashSet<>());

        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());

    }

    @Test
    void validatePostTestNullDuration() {

        Film film = new Film(1L, "test film", "description of test film",
                LocalDate.of(2004, 1, 1), 0L, new HashSet<>());

        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());

    }

    @Test
    void validatePostFilmNameIsBlank() {

        Film film = new Film(1L, "", "description of test film",
                LocalDate.of(2004, 1, 1), 120L, new HashSet<>());

        assertThrows(ValidationException.class, () -> filmController.create(film));

    }

    @Test
    void validatePutNullId() {

        Film film = new Film(1L, "test film", "description of test film",
                LocalDate.of(2004, 1, 1), 120L, new HashSet<>());

        filmController.create(film);

        Film filmUpdate = new Film();
        filmUpdate.setName("test second");
        filmUpdate.setDescription("test second description");
        filmUpdate.setReleaseDate(LocalDate.of(2005, 12, 12));
        filmUpdate.setDuration(90L);

        assertThrows(ValidationException.class, () -> filmController.update(filmUpdate));

    }

    @Test
    void validatePutNotFoundId() {
        Film film = new Film(1L, "test film", "description of test film",
                LocalDate.of(2004, 1, 1), 120L, new HashSet<>());

        filmController.create(film);

        Film filmUpdate = new Film();
        filmUpdate.setId(2L);
        filmUpdate.setName("test second");
        filmUpdate.setDescription("test second description");
        filmUpdate.setReleaseDate(LocalDate.of(2005, 12, 12));
        filmUpdate.setDuration(90L);

        assertThrows(NotFoundException.class, () -> filmController.update(filmUpdate));
    }


    @Test
    void validatePutAddLike() {

        Film film = new Film(1L, "test film", "Java — мощный язык программирования",
                LocalDate.of(2004, 1, 1), 120L, new HashSet<>());

        filmController.create(film);

        User user = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(user);

        filmController.addLike(film.getId(), user.getId());
        assertFalse(film.getLikes().isEmpty());

    }


    @Test
    void validateDeleteLike() {

        Film film = new Film(1L, "test film", "Java — мощный язык программирования",
                LocalDate.of(2004, 1, 1), 120L, new HashSet<>());

        filmController.create(film);

        User user = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(user);

        filmController.addLike(film.getId(), user.getId());

        filmController.deleteLike(film.getId(), user.getId());

        assertTrue(film.getLikes().isEmpty());

    }


    @Test
    void validateGetTopFilmsByLikes() {

        Film film = new Film(1L, "test film", "Java — мощный язык программирования",
                LocalDate.of(2004, 1, 1), 120L, new HashSet<>());

        filmController.create(film);

        Film film2 = new Film(2L, "test film2", "Java — мощный язык программирования",
                LocalDate.of(2005, 1, 1), 100L, new HashSet<>());

        filmController.create(film2);

        Film film3 = new Film(3L, "test film3", "Java — мощный язык программирования",
                LocalDate.of(2006, 1, 1), 100L, new HashSet<>());

        filmController.create(film3);

        User user = new User(1L, "test@mail.com", "test", "Name",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userController.create(user);

        User user2 = new User(2L, "test2@mail.com", "test2", "Name2",
                LocalDate.of(1950, 1, 2), new HashSet<>());

        userController.create(user2);

        User user3 = new User(3L, "test3@mail.com", "test3", "Name3",
                LocalDate.of(1970, 1, 2), new HashSet<>());

        userController.create(user3);

        filmController.addLike(film.getId(), user.getId());
        filmController.addLike(film.getId(), user2.getId());
        filmController.addLike(film.getId(), user3.getId());

        filmController.addLike(film2.getId(), user.getId());

        filmController.addLike(film3.getId(), user.getId());
        filmController.addLike(film3.getId(), user2.getId());

        List<Film> firstFilmMaxLikes = filmController.getTopFilmsByLikes(3).stream().toList();

        assertEquals(3, firstFilmMaxLikes.getFirst().getLikes().size());

    }

}
