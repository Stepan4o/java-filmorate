package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDaoImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoImpTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreDaoImpl genreDao;

    private final Genre comedy = Genre.builder()
            .id(1).name("Комедия").build();
    private final Genre drama = Genre.builder()
            .id(2).name("Драма").build();
    private final Genre cartoon = Genre.builder()
            .id(3).name("Мультфильм").build();
    private final Genre thriller = Genre.builder()
            .id(4).name("Триллер").build();
    private final Genre documentary = Genre.builder()
            .id(5).name("Документальный").build();
    private final Genre action = Genre.builder()
            .id(6).name("Боевик").build();

    @BeforeEach
    public void setUpDb() {
        genreDao = new GenreDaoImpl(jdbcTemplate);
    }

    @Test
    public void getGenreByIdTest() {
        Genre expectingComedy = genreDao.getGenreById(comedy.getId());
        Genre expectingDocumentary = genreDao.getGenreById(documentary.getId());

        assertAll(
                () -> assertNotNull(expectingComedy,
                        "Значение не вернулось"),
                () -> assertNotNull(expectingDocumentary,
                        "Значение не вернулось"),
                () -> assertEquals(comedy, expectingComedy,
                        "Ожидалось -> 'Комедия'"),
                () -> assertEquals(documentary, expectingDocumentary,
                        "Ожидалось -> 'Документальный'")
        );
    }

    @Test
    public void getAllGenresTest() {
        List<Genre> expectingGenresList =
                List.of(comedy, drama, cartoon, thriller, documentary, action);

        List<Genre> actualGenresList = genreDao.getAll();
        assertAll(
                () -> assertNotEquals(0, actualGenresList.size(),
                        "Жанры не вернулись"),
                () -> assertEquals(expectingGenresList.size(), actualGenresList.size(),
                        "Количество жанров не совпадает"),
                () -> assertEquals(comedy, actualGenresList.get(0),
                        "Ожидалось -> 'Комедия'"),
                () -> assertEquals(drama, actualGenresList.get(1),
                        "Ожидалось -> 'Драма'"),
                () -> assertEquals(cartoon, actualGenresList.get(2),
                        "Ожидалось -> 'Мульттфильм'"),
                () -> assertEquals(thriller, actualGenresList.get(3),
                        "Ожидалось -> 'Триллер'"),
                () -> assertEquals(documentary, actualGenresList.get(4),
                        "Ожидалось -> 'Документальный'"),
                () -> assertEquals(action, actualGenresList.get(5),
                        "Ожидалось -> 'Боевик'")
        );
    }
}
