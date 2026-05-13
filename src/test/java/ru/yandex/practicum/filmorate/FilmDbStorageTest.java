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
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class, FilmRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;


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


}
