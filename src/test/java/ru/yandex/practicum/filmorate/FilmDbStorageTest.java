package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.genreService.GenreDbService;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    private FilmDbStorage filmStorage;

    private final Film firstFilm = Film.builder()
            .id(1L)
            .name("firstFilm")
            .description("firstDescr")
            .releaseDate(LocalDate.of(1997, 1, 1))
            .duration(110)
            .mpa(Mpa.builder()
                    .id(1)
                    .name("G")
                    .build())
            .build();

    private final Film secondFilm = Film.builder()
            .id(2L)
            .name("secondFilm")
            .description("secondDescr")
            .releaseDate(LocalDate.of(2000, 2, 2))
            .duration(120)
            .mpa(Mpa.builder()
                    .id(2)
                    .name("PG")
                    .build())
            .build();

    private final Film thirdFilmForUpdate = Film.builder()
            .id(1L)
            .name("thirdFilm")
            .description("thirdDescr")
            .releaseDate(LocalDate.of(2000, 3, 3))
            .duration(130)
            .mpa(Mpa.builder()
                    .id(4)
                    .name("R")
                    .build())
            .genres(
                    List.of(
                            Genre.builder().id(1).name("Комедия").build(),
                            Genre.builder().id(3).name("Мультфильм").build()
                    )
            )
            .build();

    @BeforeEach
    public void setUpDb() {
        GenreDao genreDao = new GenreDaoImpl(jdbcTemplate);
        GenreDbService genreDbService = new GenreDbService(genreDao);
        filmStorage = new FilmDbStorage(jdbcTemplate, genreDbService);
        filmStorage.addFilm(firstFilm);
        filmStorage.addFilm(secondFilm);

    }

    @Test
    public void testGetFilmById() {
        Film savedFilm = filmStorage.getFilmById(firstFilm.getId());

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstFilm);
    }

    @Test
    public void testUpdateFilm() {
        Film savedFilm = filmStorage.getFilmById(firstFilm.getId());

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstFilm);

        filmStorage.updateFilm(thirdFilmForUpdate);

        savedFilm = filmStorage.getFilmById(firstFilm.getId());

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isNotEqualTo(firstFilm);
        assertThat(savedFilm)
                .usingRecursiveComparison()
                .isEqualTo(thirdFilmForUpdate);
    }

    @Test
    public void testGetAllFilmList() {
        List<Film> expectedList = List.of(firstFilm, secondFilm);
        List<Film> actualFilmList = filmStorage.getAll();

        Assertions.assertEquals(
                expectedList.size(),
                actualFilmList.size(),
                "Количество фильмом не совпадает"
        );
    }

    @Test
    public void getPopularFilmTest() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);

        User firstUser = User.builder()
                .id(1L)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userDbStorage.addUser(firstUser);

        filmStorage.addLike(secondFilm.getId(), firstUser.getId());
        List<Film> actualPopularFilmList = filmStorage.getPopular(1);
        Film actualPopularFilm = actualPopularFilmList.get(0);

        Assertions.assertEquals(secondFilm, actualPopularFilm, "Популярным должен быть secondFilm");

        filmStorage.removeLike(secondFilm.getId(), firstUser.getId());
        actualPopularFilmList = filmStorage.getPopular(1);
        actualPopularFilm = actualPopularFilmList.get(0);

        Assertions.assertNotEquals(secondFilm, actualPopularFilm, "Популярным должен быть не secondFilm");


    }

}
