package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY)" +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";

    private static final String DELETE_QUERY = "DELETE FROM USERS WHERE USER_ID = ?";

    private static final String ADD_FRIEND = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";

    private static final String GET_FRIENDS = "SELECT u.* FROM USERS u " +
            "INNER JOIN FRIENDS f ON f.FRIEND_ID = u.USER_ID " +
            "WHERE f.USER_ID = ?";

    private static final String GET_COMMON_FRIENDS = "SELECT u.* FROM USERS u " +
            "INNER JOIN FRIENDS f1 ON f1.FRIEND_ID = u.USER_ID " +
            "INNER JOIN FRIENDS f2 ON f2.FRIEND_ID = u.USER_ID " +
            "WHERE f1.USER_ID = ? AND f2.USER_ID = ?";

    private static final String DELETE_FRIEND = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public User save(User user) {
        Number id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id.longValue());
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public boolean delete(long userId) {
        update(DELETE_QUERY,
                userId);
        return true;
    }

    public void addFriend(User user, User friend) {
        update(ADD_FRIEND,
                user.getId(),
                friend.getId());
    }

    public List<User> getFriends(User user) {
        return findMany(GET_FRIENDS,
                user.getId());
    }

    public List<User> getCommonFriends(User user, User other) {
        return findMany(GET_COMMON_FRIENDS,
                user.getId(),
                other.getId());
    }

    public void deleteFriend(User user, User friend) {
        update(DELETE_FRIEND,
                user.getId(),
                friend.getId());
    }

}
