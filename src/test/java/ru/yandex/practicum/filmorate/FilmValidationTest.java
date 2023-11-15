package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.Constant.*;

public class FilmValidationTest {
    private static final LocalDate VALID_DATE_RELEASE = LocalDate.of(2000, 10, 10);
    private static final int VALID_DURATION = 1;
    private static final String MESSAGE = "Сообщения об ошибке не совпадают";

    private static FilmController filmController;
    private static UserController userController;
    private static String expectedMessage;
    private static String actualMessage;
    private static Validator validator;
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    private static final Film film_1 = Film.builder()
            .name("FilmName_Film_1")
            .description("FilmDescription_Film_1")
            .releaseDate(VALID_DATE_RELEASE)
            .duration(VALID_DURATION)
            .build();

    private static final Film filmEmptyName = Film.builder()
            .name("")
            .description("FilmDescription_Empty_Name")
            .releaseDate(VALID_DATE_RELEASE)
            .duration(VALID_DURATION)
            .build();

    private static final Film filmWithFakeDateRelease = Film.builder()
            .name("FilmName_Fake_Release")
            .description("FilmDescription_Fake_Release")
            .releaseDate(LocalDate.of(1895, 12, 27))
            .duration(VALID_DURATION)
            .build();

    private static final Film filmWithZeroDuration = Film.builder()
            .name("FilmName_Zero_Duration")
            .description("FilmDescription_Zero_Duration")
            .releaseDate(VALID_DATE_RELEASE)
            .duration(0)
            .build();

    private static final User user1 = User.builder()
            .email("oran@mail.ru")
            .name("Tomas")
            .login("Tomas99")
            .birthday(LocalDate.of(1997, 7, 17))
            .build();

    private static final User user2 = User.builder()
            .email("oran@mail.com")
            .name("Tomas")
            .login("Tomas99")
            .birthday(LocalDate.of(1997, 7, 17))
            .build();

    @BeforeEach
    void setup() {
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void fieldLikesShouldBeIncreasesByOneAfterOneLikeAndReducedAfterRemove() {
        filmController.createFilm(film_1);
        userController.createUser(user1);
        assertEquals(0, filmStorage.getFilmById(film_1.getId()).getLikes().size(), "Количество лайков не совпадает");

        filmController.addLike(film_1.getId(), user1.getId());
        assertEquals(1, filmStorage.getFilmById(film_1.getId()).getLikes().size(), "Лайк не добавлен");

        filmController.removeLike(film_1.getId(), user1.getId());
        assertEquals(0, filmStorage.getFilmById(film_1.getId()).getLikes().size(), "Лайк не удалён");

    }

    @Test
    void afterCreatedFilmShouldBeAddedInFilmStorage() {
        Collection<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Фильмов не должно существовать");

        filmController.createFilm(film_1);

        films = filmController.getFilms();

        assertEquals(1, films.size(), "Фильм не вернулся");

    }

    @Test
    void filmShouldNotBeAddedWithIncorrectName() {
        Collection<Film> actualFilms = filmController.getFilms();
        assertEquals(0, actualFilms.size(), "Фильмов не должно существовать");


        Set<ConstraintViolation<Film>> violations = validator.validate(filmEmptyName);
        for (ConstraintViolation<Film> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = INCORRECT_NAME;

        assertEquals(actualMessage, expectedMessage, "Сообщения не совпадают");
        assertEquals(0, actualFilms.size(), "Фильмов не должно существовать");

    }

    @Test
    void postMethodShouldGenerateInvalidDateTimeReleaseMessage() {
        ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(filmWithFakeDateRelease);
            }
        });
        expectedMessage = String.format(INCORRECT_RELEASE_DATE,
                MIN_DATE_RELEASE.getDayOfMonth(),
                MIN_DATE_RELEASE.getYear());
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);

    }

    @Test
    void filmShouldNotBeAddedWithIncorrectDuration() {
        Collection<Film> actualFilms = filmController.getFilms();
        assertEquals(0, actualFilms.size(), "Фильмов не должно существовать");

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithZeroDuration);
        for (ConstraintViolation<Film> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = INCORRECT_DURATION;

        assertEquals(actualMessage, expectedMessage, "Сообщения не совпадают");
        assertEquals(0, actualFilms.size(), "Фильмов не должно существовать");

    }

    @Test
    void postMethodShouldGenerateInvalidDescriptionMessage() {
        int lengthOfDescr = 201;
        StringBuilder sb = new StringBuilder(lengthOfDescr);

        while (sb.length() != lengthOfDescr) {
            sb.append("a");
        }
        String descriptionMoreThenLimit = new String(sb);

        Film outOfLimitDescription = Film.builder()
                .name("FilmName_Fake_Description")
                .description(descriptionMoreThenLimit)
                .releaseDate(VALID_DATE_RELEASE)
                .duration(VALID_DURATION)
                .build();

        Collection<Film> actualFilms = filmController.getFilms();
        assertEquals(0, actualFilms.size(), "Фильмов не должно существовать");

        Set<ConstraintViolation<Film>> violations = validator.validate(outOfLimitDescription);
        for (ConstraintViolation<Film> violation : violations) {
            actualMessage = violation.getMessage();
        }

        expectedMessage = LIMIT_DESCRIPTION;

        assertEquals(actualMessage, expectedMessage, "Сообщения не совпадают");
        assertEquals(0, actualFilms.size(), "Фильмов не должно существовать");
    }

    @Test
    void putMethodShouldGenerateInvalidIdMessage() {
        int incorrectId = 9999;
        NotFoundException exception = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(Film.builder()
                        .name("FilmName")
                        .description("FilmDescr")
                        .releaseDate(VALID_DATE_RELEASE)
                        .duration(VALID_DURATION)
                        .build());
                filmController.updateFilm(Film.builder()
                        .name("NewFilmName")
                        .description("NewFilmDescr")
                        .releaseDate(VALID_DATE_RELEASE)
                        .duration(VALID_DURATION)
                        .id(incorrectId)
                        .build());

            }
        });
        expectedMessage = String.format(FILM_NOT_FOUND, incorrectId);
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);
    }
}
