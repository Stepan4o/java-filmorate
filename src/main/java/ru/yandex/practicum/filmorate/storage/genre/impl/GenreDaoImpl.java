package ru.yandex.practicum.filmorate.storage.genre.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int id) {
        isExist(id);
        String sql = "select * from genre where genre_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);

        rowSet.next();
        return rowToGenre(rowSet);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "select * from genre";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        List<Genre> allGenres = new ArrayList<>();

        while (rowSet.next()) {
            allGenres.add(rowToGenre(rowSet));
        }
        return allGenres;
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        String sql = "SELECT * FROM genre AS g " +
                "LEFT JOIN film_genre AS fg ON g.genre_id = fg.GENRE_ID_FK " +
                "WHERE fg.film_id_fk  = ?  ORDER BY g.genre_id;";
        List<Genre> filmGenres = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        while (rowSet.next()) {
            filmGenres.add(rowToGenre(rowSet));
        }
        return filmGenres;
    }

    private static Genre rowToGenre(SqlRowSet rowSet) {
        int genreId = rowSet.getInt("genre_id");
        String name = rowSet.getString("genre_name");

        return Genre.builder()
                .id(genreId)
                .name(name)
                .build();
    }

    private void isExist(int id) {
        String sql = "select * from genre where genre_id = ?";
        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            log.error("Жанр с id {} не найден", id);
            throw new NotFoundException(String.format("Жанр с id %d не найден", id));
        }
    }
}
