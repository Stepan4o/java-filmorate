package ru.yandex.practicum.filmorate.service.mpaService;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaServiceInterface {

    Mpa getMpaById(int id);

    List<Mpa> getAll();
}
