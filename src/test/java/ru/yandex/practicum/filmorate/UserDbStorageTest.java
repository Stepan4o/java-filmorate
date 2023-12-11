package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private final LocalDate birthday = LocalDate.of(1990, 1, 1);

    private UserDbStorage userStorage;
    private User user1 = User.builder()
            .id(1L)
            .email("user1@mail.ru")
            .login("User1_login")
            .name("User1_name")
            .birthday(birthday)
            .build();

    private final User user2 = User.builder()
            .id(2L)
            .email("User2@mail.ru")
            .login("user2_login")
            .name("user2_name")
            .birthday(birthday)
            .build();

    private final User user3 = User.builder()
            .id(3L)
            .email("User3@mail.ru")
            .login("user3_login")
            .name("user3_name")
            .birthday(birthday)
            .build();

    private final User updatedUser = User.builder()
            .id(1L)
            .email("Updated@mail.ru")
            .login("Updated_login")
            .name("Updated_name")
            .birthday(birthday)
            .build();

    @BeforeEach
    public void setUpDb() {
        userStorage = new UserDbStorage(jdbcTemplate);

        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);
    }

    @Test
    public void testFindUserById() {
        User savedUser = userStorage.getUserById(user1.getId());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user1);
    }

    @Test
    public void testUpdateUser() {
        userStorage.updateUser(updatedUser);

        assertThat(user1)
                .isNotNull()
                .usingRecursiveComparison()
                .isNotEqualTo(updatedUser);

        user1 = userStorage.getUserById(user1.getId());

        assertThat(user1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    public void getFriendsListShouldBeEmptyBeforeAddingFriend() {
        List<User> expectingFriendList = new ArrayList<>();
        List<User> actualFrindList = userStorage.getFriendsList(user1.getId());

        assertEquals(0, actualFrindList.size(),
                "Список друзей должен быть пуст");

        userStorage.addFriend(user1.getId(), updatedUser.getId());
        actualFrindList = userStorage.getFriendsList(user1.getId());
        expectingFriendList.add(user1);

        assertEquals(
                expectingFriendList.size(),
                actualFrindList.size(),
                "Неверное количество друзей"
        );
    }

    @Test
    public void testGetCommonFriends() {
        List<User> expectingCommonFriendList = new ArrayList<>();
        List<User> actualCommonFriendList = userStorage.getCommonFriends(
                user1.getId(),
                user2.getId()
        );
        assertEquals(0, actualCommonFriendList.size(),
                "Список общих друзей должен быть пуст");

        userStorage.addFriend(user1.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user3.getId());

        actualCommonFriendList =
                userStorage.getCommonFriends(user1.getId(), user2.getId());
        expectingCommonFriendList.add(user3);
        System.out.println(actualCommonFriendList);

        assertEquals(
                expectingCommonFriendList.size(),
                actualCommonFriendList.size(),
                "В списке должен быть один общий друг"
        );
        assertEquals(
                actualCommonFriendList.get(0),
                expectingCommonFriendList.get(0),
                "Возвращен неверный пользователь"
        );
    }

    @Test
    public void testAddAndRemoveFriend() {
        userStorage.addFriend(user2.getId(), user3.getId());
        List<User> actualFriendsList =
                userStorage.getFriendsList(user2.getId());

        assertEquals(user3, actualFriendsList.get(0),
                "Вернулся не тот друг");

        userStorage.removeFriend(user2.getId(), user3.getId());
        actualFriendsList = userStorage.getFriendsList(user2.getId());

        assertEquals(0, actualFriendsList.size(),
                "Друзей быть не должно");
    }

    @Test
    public void getAllUsersList() {
        List<User> expectingUserList = List.of(user1, user2, user3);
        List<User> actualUsersList = userStorage.getAll();
        assertEquals(
                expectingUserList.size(),
                actualUsersList.size(),
                "Неверное количство пользователей"
        );
    }
}
