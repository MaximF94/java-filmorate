package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class FilmValidateTests {

    private static FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void validatePostTestDescriptionMaxSymbols() {

        Film film = new Film(1L, "test film", "Java — мощный язык программирования, используемый для " +
                "создания надёжных приложений. Он поддерживает ООП, многопоточность и работает " +
                "на разных платформах. Изучать Java интересно и полезно для разработч",
                LocalDate.of(2004, 1, 1), 120L);

        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());

    }

    @Test
    void validatePostTestEarlyDateFilm() {

        Film film = new Film(1L, "first film", "description of first film",
                LocalDate.of(1895, 12, 28), 120L);

        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());

    }

    @Test
    void validatePostTestNullDuration() {

        Film film = new Film(1L, "test film", "description of test film",
                LocalDate.of(2004, 1, 1), 0L);

        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());

    }

    @Test
    void validatePostFilmNameIsBlank() {

        Film film = new Film(1L, "", "description of test film",
                LocalDate.of(2004, 1, 1), 120L);

        assertThrows(ValidationException.class, () -> filmController.create(film));

    }

    @Test
    void validatePutNullId() {

        Film film = new Film(1L, "test film", "description of test film",
                LocalDate.of(2004, 1, 1), 120L);

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
                LocalDate.of(2004, 1, 1), 120L);

        filmController.create(film);

        Film filmUpdate = new Film();
        filmUpdate.setId(2L);
        filmUpdate.setName("test second");
        filmUpdate.setDescription("test second description");
        filmUpdate.setReleaseDate(LocalDate.of(2005, 12, 12));
        filmUpdate.setDuration(90L);

        assertThrows(NotFoundException.class, () -> filmController.update(filmUpdate));
    }

}
