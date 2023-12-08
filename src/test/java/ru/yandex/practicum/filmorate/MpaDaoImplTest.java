package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.impl.MpaDaoImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.filmorate.Constant.CREATE_TABLE_FOR_MPA_TEST;
import static ru.yandex.practicum.filmorate.Constant.DROP_ALL_TABLES;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoImplTest {
    private final JdbcTemplate jdbcTemplate;
    private MpaDaoImpl mpaDao;
    private final Mpa mpaG = Mpa.builder().id(1).name("G").build();
    private final Mpa mpaPG = Mpa.builder().id(2).name("PG").build();
    private final Mpa mpaPG13 = Mpa.builder().id(3).name("PG-13").build();
    private final Mpa mpaR = Mpa.builder().id(4).name("R").build();
    private final Mpa mpaNC17 = Mpa.builder().id(5).name("NC-17").build();


    @BeforeEach
    public void setUpDb() {
        String sql = DROP_ALL_TABLES + CREATE_TABLE_FOR_MPA_TEST;
        jdbcTemplate.update(sql);
        mpaDao = new MpaDaoImpl(jdbcTemplate);
    }

    @Test
    public void getMpaByIdTest() {
        Mpa actualMpaG = mpaDao.getMpaById(mpaG.getId());
        Mpa actualMpaNC17 = mpaDao.getMpaById(mpaNC17.getId());

        assertAll(
                () -> assertEquals(mpaG, actualMpaG,
                        "Ожидался рейтинг -> 'G'"),
                () -> assertEquals(mpaNC17, actualMpaNC17,
                        "Ожидался рейтинг -> 'NC-17'")
        );
    }

    @Test
    public void getListMpaTest() {
        List<Mpa> expectingListOfMpa =
                List.of(mpaG, mpaPG, mpaPG13, mpaR, mpaNC17);

        List<Mpa> actualListOfMpa = mpaDao.getAll();

        assertAll(
                () -> assertEquals(expectingListOfMpa.size(), actualListOfMpa.size(),
                        "Количество не совпадает"),
                () -> assertEquals(expectingListOfMpa.get(0), actualListOfMpa.get(0),
                        "Ожидался рейтинг -> 'G'"),
                () -> assertEquals(expectingListOfMpa.get(1), actualListOfMpa.get(1),
                        "Ожидался рейтинг -> 'PG'"),
                () -> assertEquals(expectingListOfMpa.get(2), actualListOfMpa.get(2),
                        "Ожидался рейтинг -> 'PG-13'"),
                () -> assertEquals(expectingListOfMpa.get(3), actualListOfMpa.get(3),
                        "Ожидался рейтинг -> 'R'"),
                () -> assertEquals(expectingListOfMpa.get(4), actualListOfMpa.get(4),
                        "Ожидался рейтинг -> 'NC-17'")

        );
    }
}
