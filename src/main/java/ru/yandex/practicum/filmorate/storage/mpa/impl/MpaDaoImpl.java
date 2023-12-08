package ru.yandex.practicum.filmorate.storage.mpa.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "select * from mpa where mpa_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            int mpaId = rowSet.getInt("mpa_id");
            String mpaName = rowSet.getString("mpa_name");
            return Mpa.builder()
                    .id(mpaId)
                    .name(mpaName)
                    .build();
        } else {
            throw new NotFoundException(String.format("MPA с id %d не найден", id));
        }
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
}
