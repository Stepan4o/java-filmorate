package ru.yandex.practicum.filmorate.service.genreService;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.List;

@Service
public class GenreDbService implements GenreServiceInterface {

    private final GenreDao genreDao;

    public GenreDbService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre getGenreById(int id) {
        return genreDao.getGenreById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        return genreDao.getGenresByFilmId(filmId);
    }
}
