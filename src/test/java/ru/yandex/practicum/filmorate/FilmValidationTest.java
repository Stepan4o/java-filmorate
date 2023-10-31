package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmModelException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTest {
    private static final LocalDate VALID_DATE_RELEASE = LocalDate.of(2000, 10, 10);
    private static final int VALID_DURATION = 1;
    private static final String MESSAGE = "Сообщения об ошибке не совпадают";

    private static FilmController controller;
    private static InvalidFilmModelException exception;
    private static String expectedMessage;

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

    @BeforeEach
    void setup() {
        controller = new FilmController();
    }

    @Test
    void afterCreatedFilmShouldBeAddedInFilmStorage() {
        Collection<Film> films = controller.findAll();
        assertEquals(0, films.size(), "Фильмов не должно существовать");

        controller.createFilm(film_1);

        films = controller.findAll();

        assertEquals(1, films.size(), "Фильм не вернулся");

    }

    @Test
    void postMethodShouldGenerateInvalidNameMessage() {
        exception = assertThrows(InvalidFilmModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createFilm(filmEmptyName);
            }
        });

        expectedMessage = InvalidFilmModelException.INCORRECT_NAME;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);

    }

    @Test
    void postMethodShouldGenerateInvalidDateTimeReleaseMessage() {
        exception = assertThrows(InvalidFilmModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createFilm(filmWithFakeDateRelease);
            }
        });
        expectedMessage = InvalidFilmModelException.INCORRECT_RELEASE_DATE;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);

    }

    @Test
    void postMethodShouldGenerateInvalidDurationMessage() {
        exception = assertThrows(InvalidFilmModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createFilm(filmWithZeroDuration);
            }
        });
        expectedMessage = InvalidFilmModelException.INCORRECT_DURATION;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);

    }

    @Test
    void postMethodShouldGenerateInvalidDescriptionMessage() {
        int lengthOfDescr = 201;
        StringBuilder sb = new StringBuilder(lengthOfDescr);

        while (sb.length() != lengthOfDescr) {
            sb.append("a");
        }
        String descriptionMoreThenLimit = new String(sb);

        exception = assertThrows(InvalidFilmModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createFilm(Film.builder()
                        .name("FilmName_Fake_Description")
                        .description(descriptionMoreThenLimit)
                        .releaseDate(VALID_DATE_RELEASE)
                        .duration(VALID_DURATION)
                        .build());
            }
        });
        expectedMessage = InvalidFilmModelException.LIMIT_DESCRIPTION;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);

    }

    @Test
    void putMethodShouldGenerateInvalidIdMessage() {
        exception = assertThrows(InvalidFilmModelException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.createFilm(Film.builder()
                        .name("FilmName")
                        .description("FilmDescr")
                        .releaseDate(VALID_DATE_RELEASE)
                        .duration(VALID_DURATION)
                        .build());
                controller.updateFilm(Film.builder()
                        .name("NewFilmName")
                        .description("NewFilmDescr")
                        .releaseDate(VALID_DATE_RELEASE)
                        .duration(VALID_DURATION)
                        .id(9999)
                        .build());

            }
        });
        expectedMessage = InvalidFilmModelException.INCORRECT_ID;
        assertEquals(expectedMessage, exception.getMessage(), MESSAGE);
    }
}
