package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM GENRES";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM GENRES WHERE GENRE_ID = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> findById(long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    public Set<Long> findExistingIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                placeholders.append(", ");
            }
            placeholders.append("?");
        }

        String sql = "SELECT GENRE_ID FROM GENRES WHERE GENRE_ID IN (" + placeholders.toString() + ")";

        List<Long> existingIds = jdbc.queryForList(sql, Long.class, ids.toArray());
        return new HashSet<>(existingIds);
    }

}
