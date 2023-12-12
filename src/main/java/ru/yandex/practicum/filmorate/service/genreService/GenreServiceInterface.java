package ru.yandex.practicum.filmorate.service.genreService;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreServiceInterface {

    Genre getGenreById(int id);

    List<Genre> getAll();

    List<Genre> getGenresByFilmId(long filmId);


}
