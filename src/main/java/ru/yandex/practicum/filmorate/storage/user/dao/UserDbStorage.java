package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.Constant.*;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        isExistEmail(USER_TABLE, EMAIL, user.getEmail());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId((long) insert.executeAndReturnKey(user.userToMap()).intValue());
        log.info("User c id {} создан", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        isExistId(USER_TABLE, USER_ID, user.getId());
        isExistEmail(USER_TABLE, EMAIL, user.getEmail());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sql = "update users set user_name = ?, login = ?, birthday = ?, " +
                "email = ?  where user_id = ?";
        jdbcTemplate.update(
                sql,
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getEmail(),
                user.getId()
        );
        log.info("User с id {} обновлён", user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from users";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        List<User> users = new ArrayList<>();
        while (rowSet.next()) {
            User user = rowToUser(rowSet);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getFriendsList(Long id) {
        isExistId(USER_TABLE, USER_ID, id);
        String sql = "select f.FRIEND_ID_FK AS user_id, " +
                "u.email, " +
                "u.login, " +
                "u.user_name, " +
                "u.birthday " +
                "FROM FRIENDSHIP f " +
                "JOIN USERS u ON u.user_id = f.FRIEND_ID_FK WHERE USER_ID_FK = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        List<User> friends = new ArrayList<>();
        while (rowSet.next()) {
            friends.add(rowToUser(rowSet));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        isExistId(USER_TABLE, USER_ID, id);
        isExistId(USER_TABLE, USER_ID, otherId);
        String sql = "SELECT * " +
                "FROM USERS " +
                "WHERE USER_ID IN " +
                "(SELECT f.friend_id_fk " +
                "FROM FRIENDSHIP f " +
                "LEFT JOIN FRIENDSHIP f2 ON f2.FRIEND_ID_FK = f.friend_id_fk " +
                "WHERE f.user_id_fk = ? AND f2.USER_ID_FK = ?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id, otherId);
        List<User> comFriends = new ArrayList<>();
        while (rowSet.next()) {
            comFriends.add(rowToUser(rowSet));
        }
        return comFriends;
    }

    @Override
    public User getUserById(Long id) {
        isExistId(USER_TABLE, USER_ID, id);
        String sql = "select * from users where user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        rowSet.next();

        log.info("User c id {} получен", id);
        return rowToUser(rowSet);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        isExistId(USER_TABLE, USER_ID, id);
        isExistId(USER_TABLE, USER_ID, friendId);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("friendship")
                .usingColumns("user_id_fk", "friend_id_fk");
        insert.execute(Map.of("user_id_fk", id, "friend_id_fk", friendId));
        log.info("Пользоватесь с id {} добваил в друзья пользователя с id {}", id, friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        isExistId(USER_TABLE, USER_ID, id);
        isExistId(USER_TABLE, USER_ID, friendId);
        String sql = "delete from FRIENDSHIP WHERE USER_ID_FK = ? AND FRIEND_ID_FK = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    private void isExistId(String table, String str_id, Long id) {
        String sql = "select * from " + table + " where " + str_id + " = ?";

        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            log.error(String.format(USER_NOT_FOUND, id));
            throw new NotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }

    private void isExistEmail(String table, String str_email, String value) {
        String sql = "select * from " + table + " where " + str_email + " = ?";

        if (jdbcTemplate.queryForRowSet(sql, value).next()) {
            log.error(String.format(ALREADY_EXIST_EMAIL, value));
            throw new AlreadyExistException(String.format(ALREADY_EXIST_EMAIL, value));
        }
    }

    private User rowToUser(SqlRowSet rowSet) {
        long id = rowSet.getLong("USER_ID");
        String name = rowSet.getString("user_name");
        String login = rowSet.getString("login");
        String email = rowSet.getString("email");
        LocalDate birthday = Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate();

        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .birthday(birthday)
                .login(login)
                .build();
    }
}
