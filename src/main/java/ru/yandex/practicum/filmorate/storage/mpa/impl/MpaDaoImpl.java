package ru.yandex.practicum.filmorate.storage.mpa.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        isExist(id);
        String sql = "select * from mpa where mpa_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        rowSet.next();
        int mpaId = rowSet.getInt("mpa_id");
        String mpaName = rowSet.getString("mpa_name");
        return Mpa.builder()
                .id(mpaId)
                .name(mpaName)
                .build();
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "select * from mpa";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        List<Mpa> res = new ArrayList<>();
        while (rowSet.next()) {
            Mpa mpa = rowToMpa(rowSet);
            res.add(mpa);
        }
        return res;
    }

    private Mpa rowToMpa(SqlRowSet rowSet) {
        int id = rowSet.getInt("mpa_id");
        String name = rowSet.getString("mpa_name");

        return Mpa.builder()
                .id(id)
                .name(name)
                .build();
    }

    private void isExist(int id) {
        String sql = "select * from mpa where mpa_id = ?";
        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            log.error("Рейтинг с id {} не найден", id);
            throw new NotFoundException(String.format("Рейтинг с id %d не найден", id));
        }
    }
}
