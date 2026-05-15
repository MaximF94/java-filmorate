package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    private final JdbcTemplate jdbcTemplate;

    public FilmRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        Long filmId = resultSet.getLong("film_id");
        film.setId(filmId);
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));

        Date releaseDate = resultSet.getDate("releasedate");
        if (releaseDate != null) {
            film.setReleaseDate(releaseDate.toLocalDate());
        }

        film.setDuration(resultSet.getLong("duration"));

        Set<Long> likes = getLikesForFilm(filmId);
        film.setLikes(likes != null ? likes : new HashSet<>());

        Long mpaRatingId = resultSet.getLong("MPA_RATING_ID");
        if (!resultSet.wasNull()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRatingId);
            mpa.setName(resultSet.getString("MPA_RATING_NAME"));  // ← из JOIN
            film.setMpa(mpa);
        }

        Set<Genre> genres = getGenresForFilm(filmId);
        film.setGenres(genres != null ? genres : new HashSet<>());

        return film;
    }

    private Set<Long> getLikesForFilm(Long filmId) {
        String sql = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID = ?";
        List<Long> userIds = jdbcTemplate.queryForList(sql, Long.class, filmId);
        return new HashSet<>(userIds);
    }


    private Set<Genre> getGenresForFilm(Long filmId) {
        String sql = "SELECT g.GENRE_ID, g.NAME FROM GENRES g JOIN FILMS_GENRES fg ON g.GENRE_ID = fg.GENRE_ID " +
                "WHERE fg.FILM_ID = ? ORDER BY g.GENRE_ID";

        List<Genre> genres = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(resultSet.getLong("GENRE_ID"));
            genre.setName(resultSet.getString("NAME"));
            return genre;
        }, filmId);

        return new HashSet<>(genres);
    }
}
