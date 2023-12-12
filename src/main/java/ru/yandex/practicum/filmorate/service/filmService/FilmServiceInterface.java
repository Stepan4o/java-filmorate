package ru.yandex.practicum.filmorate.service.filmService;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {

    List<Film> getAll();

    Film getFilmById(Long id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Long id, Long userId);

    void removeLike(Long id, Long userId);

    List<Film> getPopular(int count);

}
