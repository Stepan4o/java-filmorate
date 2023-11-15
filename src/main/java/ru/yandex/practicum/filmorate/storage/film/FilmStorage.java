package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> getFilms();

    Film getFilmById(long id);

    Film createFilm(Film film);

    Film addLike(long id, long userId);

    Film removeLike(long id, long userId);

    Film updateFilm(Film film);

    List<Film> getPopular(int count);
}
