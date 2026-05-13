package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, " +
            "f.MPA_RATING_ID, mr.NAME AS MPA_RATING_NAME FROM FILMS f LEFT JOIN MPA_RATINGS " +
            "mr ON f.MPA_RATING_ID = mr.MPA_RATING_ID ORDER BY f.FILM_ID";

    private static final String FIND_BY_ID_QUERY = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, " +
            "f.MPA_RATING_ID, mr.NAME AS MPA_RATING_NAME FROM FILMS f LEFT JOIN MPA_RATINGS " +
            "mr ON f.MPA_RATING_ID = mr.MPA_RATING_ID WHERE f.FILM_ID = ? ORDER BY f.FILM_ID";

    private static final String INSERT_QUERY = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_RATING_ID)" +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_GENRE_QUERY = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";

    private static final String DELETE_GENRES_QUERY = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";

    private static final String UPDATE_QUERY = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, " +
            "DURATION = ?, MPA_RATING_ID = ? WHERE FILM_ID = ?";

    private static final String ADD_LIKE = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";

    private static final String DELETE_LIKE = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";

    private static final String GET_TOP_FILMS_BY_LIKES = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, " +
            "f.MPA_RATING_ID, mr.NAME AS MPA_RATING_NAME FROM FILMS f LEFT JOIN MPA_RATINGS " +
            "mr ON f.MPA_RATING_ID = mr.MPA_RATING_ID ORDER BY (SELECT COUNT(*) FROM FILM_LIKES " +
            "WHERE FILM_ID = f.FILM_ID) DESC LIMIT ?";

    private static final String DELETE_QUERY = "DELETE FROM FILMS WHERE FILM_ID = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public Film save(Film film) {
        Number id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null
        );
        film.setId(id.longValue());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            saveGenres(film.getId(), film.getGenres());
        }

        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId()
        );

        deleteGenres(film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            saveGenres(film.getId(), film.getGenres());
        }

        return film;
    }

    public void addLike(Film film, User user) {
        update(ADD_LIKE,
                film.getId(),
                user.getId());
    }

    public void deleteLike(Film film, User user) {
        update(DELETE_LIKE,
                film.getId(),
                user.getId());
    }

    public List<Film> getTopFilmsByLikes(Integer count) {
        return findMany(GET_TOP_FILMS_BY_LIKES, count);
    }

    public boolean deleteFilm(Long id) {
        update(DELETE_QUERY, id);
        return true;
    }

    private void saveGenres(Long filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbc.update(INSERT_GENRE_QUERY, filmId, genre.getId());
        }
    }

    private void deleteGenres(Long filmId) {
        jdbc.update(DELETE_GENRES_QUERY, filmId);
    }


}
