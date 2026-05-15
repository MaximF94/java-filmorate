package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class, FilmRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;


    private Film createFilm() {
        Film film = new Film();
        film.setName("Test name");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(1991, 1, 1));
        film.setDuration(120L);
        film.setMpa(createMpa(1L));
        film.setGenres(Set.of(createGenre(1L), createGenre(2L)));
        return film;
    }

    private Mpa createMpa(Long id) {
        Mpa mpa = new Mpa();
        mpa.setId(id);
        return mpa;
    }

    private Genre createGenre(Long id) {
        Genre genre = new Genre();
        genre.setId(id);
        return genre;
    }


    @Test
    void testFindFilmById() {
        Optional<Film> filmOptional = filmStorage.findById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(1L);
                });
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Film> filmOptional = filmStorage.findById(10000L);
        assertThat(filmOptional).isEmpty();
    }


    @Test
    void testCreateFilm() {
        Film createdFilm = filmStorage.save(createFilm());
        assertThat(createdFilm.getId()).isNotNull();
    }

    @Test
    void testUpdateFilm() {
        Optional<Film> oldFilm = filmStorage.findById(1L);

        oldFilm.get().setDescription("new description");

        Film updatedFilm = filmStorage.save(oldFilm.get());

        assertThat(updatedFilm.getDescription().contains("new description"));
    }

    @Test
    void testIsExistsGenresCount() {
        Optional<Film> oldFilm = filmStorage.findById(1L);

        assertThat(oldFilm.get().getGenres().size() == 2);
    }

    @Test
    void testIsExistsMpa() {
        Optional<Film> oldFilm = filmStorage.findById(1L);

        assertThat(oldFilm.get().getMpa().getName().equals("R"));
    }

    @Test
    void testLimitPopularFilmsCount() {
        Collection<Film> popularFilms = filmStorage.getTopFilmsByLikes(2);
        assertThat(popularFilms.size() == 2);
    }


}
