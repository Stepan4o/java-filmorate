package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.genreService.GenreDbService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constant.*;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbService genreDbService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbService genreDbService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbService = genreDbService;
    }

    @Override
    public Film getFilmById(Long id) {
        isExist(FILM_TABLE, FILM_ID, id);
        String sql = "select f.*, m.mpa_name from films f join mpa m on f.mpa_id_fk = m.mpa_id where f.film_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        rowSet.next();

        Film film = rowToFilm(rowSet);
        log.info("Найден фильм с id {}", id);
        return film;

    }


    @Override
    public List<Film> getAll() {
        String sql = "select f.*, m.mpa_name from films f join mpa m on f.mpa_id_fk = m.mpa_id";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();

        while (rowSet.next()) {
            films.add(rowToFilm(rowSet));
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        dateReleaseValidation(film.getReleaseDate());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILM_TABLE)
                .usingGeneratedKeyColumns(FILM_ID);
        film.setId((long) insert.executeAndReturnKey(film.filmToMap()).intValue());

        if (film.getGenres() == null) {
            delGenresFromFilmsTable(film.getId());
            film.setGenres(new ArrayList<>());
            log.info("Фильм с id {} добавлен", film.getId());
            return film;
        } else {
            delGenresFromFilmsTable(film.getId());
            List<Genre> genres = new ArrayList<>(film.getGenres());
            List<Integer> genreIds = new ArrayList<>();
            for (Genre genre : genres) {
                genreIds.add(genre.getId());
            }
            addGenresToFilm(genreIds, film.getId());
        }

        log.info("Фильм с id {} добавлен", film.getId());
        return film;
    }

    private void delGenresFromFilmsTable(Long filmId) {
        String sql = "delete from film_genre where film_id_fk = ?";
        jdbcTemplate.update(sql, filmId);
    }


    @Override
    public void addLike(Long id, Long userId) {

        isExist(FILM_TABLE, FILM_ID, id);
        isExist(USER_TABLE, USER_ID, userId);
        String sql = "insert into likes (film_id_fk, user_id_fk) values (?,?)";
        jdbcTemplate.update(sql, id, userId);
        log.info("User c id {} поставил лайк фильму с id {}", id, userId);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        isExist(FILM_TABLE, FILM_ID, id);
        isExist(USER_TABLE, USER_ID, userId);
        String sql = "delete from likes " +
                "where film_id_fk = ? " +
                "and user_id_fk = ?";
        jdbcTemplate.update(sql, id, userId);
        log.info("User c id {} удалил лайк у фильма c id {}", userId, id);
    }

    private void addGenresToFilm(List<Integer> genreIds, Long filmId) {
        System.out.println(genreIds + " " + filmId);
        if (!genreIds.isEmpty()) {
            for (Integer id : genreIds) {
                int genreId = id;
                String sql = "insert into film_genre (film_id_fk, genre_id_fk) values (?, ?)";
                jdbcTemplate.update(sql, filmId, genreId);
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        isExist(FILM_TABLE, FILM_ID, film.getId());
        String sql = "update films set film_name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id_fk = ? where film_id = ?";
        jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        if (film.getGenres() == null) {
            delGenresFromFilmsTable(film.getId());
            film.setGenres(new ArrayList<>());
            return film;
        } else {
            delGenresFromFilmsTable(film.getId());
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
            List<Integer> genreIds = film.getGenres().stream().map(Genre::getId).collect(Collectors.toList());

            addGenresToFilm(genreIds, film.getId());
        }

        log.info("Фильм с id {} обновлен", film.getId());
        return film;
    }

    @Override
    public List<Film> getPopular(int count) {
        String sql = "select f.*, " +
                " m.mpa_name, " +
                " count(l.USER_ID_FK) rate " +
                "from films f " +
                "LEFT JOIN LIKES l ON l.FILM_ID_FK = f.FILM_ID " +
                "LEFT JOIN MPA m ON m.mpa_id = f.mpa_id_fk " +
                "GROUP BY f.film_id " +
                "ORDER BY rate DESC " +
                " limit ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, count);
        List<Film> films = new ArrayList<>();
        while (rowSet.next()) {
            films.add(rowToFilm(rowSet));
        }
        return films;
    }

    private Film rowToFilm(SqlRowSet rowSet) {
        long filmId = rowSet.getLong("film_id");
        String name = rowSet.getString("film_name");
        String description = rowSet.getString("description");
        LocalDate releaseDate = Objects.requireNonNull(rowSet.getDate("release_date")).toLocalDate();
        int duration = rowSet.getInt("duration");
        int mpaId = rowSet.getInt("mpa_id_fk");
        String mpaName = rowSet.getString("mpa_name");

        Mpa mpa = Mpa.builder()
                .id(mpaId)
                .name(mpaName)
                .build();

        return Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .genres(genreDbService.getGenresByFilmId(filmId))
                .build();
    }


    private void dateReleaseValidation(LocalDate releaseDate) {
        if (releaseDate.isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException(String.format(INCORRECT_RELEASE_DATE,
                    MIN_DATE_RELEASE.getDayOfMonth(),
                    MIN_DATE_RELEASE.getYear()));
        }
    }

    private void isExist(String table, String str_id, Long id) {
        String sql = "select * from " + table + " where " + str_id + " = ?";

        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            throw new NotFoundException("Не найдено по айди");
        }
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }
}
