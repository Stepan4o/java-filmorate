package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getAll();

    Film getFilmById(Long id);

    Film addFilm(Film film);

    void addLike(Long id, Long userId);

    void removeLike(Long id, Long userId);

    Film updateFilm(Film film);

    List<Film> getPopular(int count);
}
