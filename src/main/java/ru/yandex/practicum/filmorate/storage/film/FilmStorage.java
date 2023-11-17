package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> getFilms();

    Film getFilmById(Long id);

    Film createFilm(Film film);

    Film addLike(Long id, Long userId);

    Film removeLike(Long id, Long userId);

    Film updateFilm(Film film);

    List<Film> getPopular(int count);
}
