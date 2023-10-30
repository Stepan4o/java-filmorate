package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.InvalidUserModelException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest {
    private static UserController controller;
    private static final LocalDate VALID_BIRTHDAY = LocalDate.of(1997, 7, 17);
    private static final String VALID_MAIL = "mail@email.ru";
    private static final String VALID_NAME = "Name";
    private static final String VALID_LOGIN = "Login";
    private static final String MESSAGE = "Сообщения об ошибке не совпадают";
    private static String expectedMessage;
    private static InvalidUserModelException exception;

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
            .birthday(LocalDate.of(2025, 10, 10))
            .build();

    private final User incorrectMail = User.builder()
            .name(VALID_NAME)
            .email("mail")
            .login(VALID_LOGIN)
            .birthday(VALID_BIRTHDAY)
            .build();

    private final User incorrectLogin = User.builder()
            .name(VALID_NAME)
            .email(VALID_MAIL)
            .login("log in")
            .birthday(VALID_BIRTHDAY)
            .build();

    private final User nullName = User.builder()
            .name(null)
            .email(VALID_MAIL)
            .login(VALID_LOGIN)
            .birthday(VALID_BIRTHDAY)
            .build();

    @BeforeEach
    void setup() {
        controller = new UserController();
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
    void postMethodShouldGenerateInvalidMailMessage() {
        exception = assertThrows(InvalidUserModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createUser(incorrectMail);
            }
        });
        expectedMessage = InvalidUserModelException.INCORRECT_EMAIL;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);
    }

    @Test
    void postMethodShouldGenerateInvalidLoginMessage() {
        exception = assertThrows(InvalidUserModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createUser(incorrectLogin);
            }
        });
        expectedMessage = InvalidUserModelException.INCORRECT_LOGIN;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);
    }

    @Test
    void postMethodShouldGenerateInvalidBirthdayMessage() {
        exception = assertThrows(InvalidUserModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createUser(incorrectBirthday);
            }
        });
        expectedMessage = InvalidUserModelException.INCORRECT_BIRTHDAY;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);
    }
}

