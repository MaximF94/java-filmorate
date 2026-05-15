package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper<User> {

    private final JdbcTemplate jdbcTemplate;

    public UserRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        Long userId = resultSet.getLong("user_id");
        user.setId(userId);
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));

        Date birthday = resultSet.getDate("birthday");
        if (birthday != null) {
            user.setBirthday(birthday.toLocalDate());
        }

        Set<Long> friends = getFriendsForUser(userId);
        user.setFriends(friends != null ? friends : new HashSet<>());

        return user;
    }

    private Set<Long> getFriendsForUser(Long userId) {
        String sql = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
        List<Long> userIds = jdbcTemplate.queryForList(sql, Long.class, userId);
        return new HashSet<>(userIds);
    }
}
