package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.InvalidUserModelException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest {
    private static UserController controller;
    private static final LocalDate VALID_BIRTHDAY = LocalDate.of(1997, 7, 17);
    private static final String VALID_MAIL = "mail@email.ru";
    private static final String VALID_NAME = "Name";
    private static final String VALID_LOGIN = "Login";
    private static String expectedMessage;
    private static String actualMessage;
    private static Validator validator;

    private final User validUser = User.builder()
            .name(VALID_NAME)
            .email(VALID_MAIL)
            .login(VALID_LOGIN)
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
        controller = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void userShouldNotCreatedWithIncorrectEmail() {
        Collection<User> actualUsers = controller.findAll();
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

        Set<ConstraintViolation<User>> violations = validator.validate(invalidEmail);
        for (ConstraintViolation<User> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = InvalidUserModelException.INCORRECT_EMAIL;

        assertEquals(actualMessage, expectedMessage, "Сообщения не совпадают");
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

    }

    @Test
    void shouldBeThrowExceptionWithIncorrectLogin() {
        assertThrows(InvalidUserModelException.class,
                () -> controller.createUser(invalidLogin));
    }

    @Test
    void afterCreatedUserShouldBeAddedInUserStorage() {
        Collection<User> users = controller.findAll();
        assertEquals(0, users.size(), "Пользователей не должно существовать");

        controller.createUser(validUser);

        users = controller.findAll();

        assertEquals(1, users.size(), "Пользователь не вернулся");
    }

    @Test
    void userShouldNotBeCreatedWithEmptyLogin() {
        Collection<User> actualUsers = controller.findAll();
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

        Set<ConstraintViolation<User>> violations = validator.validate(emptyLogin);
        for (ConstraintViolation<User> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = InvalidUserModelException.INCORRECT_LOGIN;

        assertEquals(expectedMessage, actualMessage, "Сообщения не совпадают");
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");
    }

    @Test
    void userShouldNotBeCreatedWithNullOrFutureBirthday() {
        Collection<User> actualUsers = controller.findAll();
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");


        Set<ConstraintViolation<User>> violations = validator.validate(incorrectBirthday);
        for (ConstraintViolation<User> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = InvalidUserModelException.INCORRECT_BIRTHDAY;

        assertEquals(actualMessage, expectedMessage, "Сообщения не совпадают");
        assertEquals(0, actualUsers.size(), "Пользователей не должно существовать");

    }
}

