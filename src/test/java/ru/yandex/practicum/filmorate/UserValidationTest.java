package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.Constant.*;

public class UserValidationTest {
    private static UserController controller;
    private static final LocalDate VALID_BIRTHDAY = LocalDate.of(1997, 7, 17);
    private static final String VALID_MAIL = "mail@email.ru";
    private static final String VALID_NAME = "Name";
    private static final String VALID_LOGIN = "Login";
    private static String expectedMessage;
    private static String actualMessage;
    private static Validator validator;
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    private final User validUser1 = User.builder()
            .name(VALID_NAME)
            .email(VALID_MAIL)
            .login(VALID_LOGIN)
            .birthday(VALID_BIRTHDAY)
            .build();

    private final User validUser2 = User.builder()
            .name("name2")
            .email("ru@maul.ru")
            .login("Login2")
            .birthday(VALID_BIRTHDAY)
            .build();

    private final User incorrectBirthday = User.builder()
            .name(VALID_NAME)
            .email(VALID_MAIL)
            .login(VALID_LOGIN)
            .birthday(null)
            .build();

    private final User emptyLogin = User.builder()
            .name(VALID_NAME)
            .email(VALID_MAIL)
            .login("")
            .birthday(VALID_BIRTHDAY)
            .build();

    private final User invalidLogin = User.builder()
            .name(VALID_NAME)
            .email(VALID_MAIL)
            .login("l o g i n")
            .birthday(VALID_BIRTHDAY)
            .build();

    private final User invalidEmail = User.builder()
            .name(VALID_NAME)
            .email("invalidEmail")
            .login(VALID_LOGIN)
            .birthday(VALID_BIRTHDAY)
            .build();

    @BeforeEach
    void setup() {
        UserService userService = new UserService(userStorage);
        controller = new UserController(userService);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void userShouldNotCreatedWithIncorrectEmail() {
        Collection<User> actualUsers = controller.getUsers();
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

        Set<ConstraintViolation<User>> violations = validator.validate(invalidEmail);
        for (ConstraintViolation<User> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = INCORRECT_EMAIL;

        assertEquals(actualMessage, expectedMessage, "Сообщения не совпадают");
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

    }

    @Test
    void shouldBeThrowExceptionWithIncorrectLogin() {
        assertThrows(ValidationException.class,
                () -> controller.createUser(invalidLogin));
    }

    @Test
    void afterCreatedUserShouldBeAddedInUserStorage() {
        Collection<User> users = controller.getUsers();
        assertEquals(0, users.size(), "Пользователей не должно существовать");

        controller.createUser(validUser1);

        users = controller.getUsers();

        assertEquals(1, users.size(), "Пользователь не вернулся");
    }

    @Test
    void userShouldNotBeCreatedWithEmptyLogin() {
        Collection<User> actualUsers = controller.getUsers();
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

        Set<ConstraintViolation<User>> violations = validator.validate(emptyLogin);
        for (ConstraintViolation<User> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = INCORRECT_LOGIN;

        assertEquals(expectedMessage, actualMessage, "Сообщения не совпадают");
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");
    }

    @Test
    void userShouldNotBeCreatedWithNullOrFutureBirthday() {
        Collection<User> actualUsers = controller.getUsers();
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");


        Set<ConstraintViolation<User>> violations = validator.validate(incorrectBirthday);
        for (ConstraintViolation<User> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = INCORRECT_BIRTHDAY;

        assertEquals(actualMessage, expectedMessage, "Сообщения не совпадают");
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

    }

    @Test
    void fieldFriendsShouldBeIncreasedByOneAfterAddedFriendAndReducedAfterRemove() {
        controller.createUser(validUser1);
        controller.createUser(validUser2);

        int countFriendsUser1 = userStorage.getUserById(validUser1.getId()).getFriends().size();
        int countFriendsUser2 = userStorage.getUserById(validUser2.getId()).getFriends().size();

        String msg = "Список должен быть пустой";
        assertEquals(0, countFriendsUser1, msg);
        assertEquals(0, countFriendsUser2, msg);

        controller.addFriend(validUser1.getId(), validUser2.getId());

        countFriendsUser1 = userStorage.getUserById(validUser1.getId()).getFriends().size();
        countFriendsUser2 = userStorage.getUserById(validUser2.getId()).getFriends().size();

        String msg1 = "Должен быть только один дуруг";
        assertEquals(1, countFriendsUser1, msg1);
        assertEquals(1, countFriendsUser2, msg1);

        controller.removeFriend(validUser1.getId(), validUser2.getId());

        countFriendsUser1 = userStorage.getUserById(validUser1.getId()).getFriends().size();
        countFriendsUser2 = userStorage.getUserById(validUser2.getId()).getFriends().size();

        assertEquals(0, countFriendsUser1, msg);
        assertEquals(0, countFriendsUser2, msg);
    }
}

