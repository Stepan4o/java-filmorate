package ru.yandex.practicum.filmorate.service.filmService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constant.*;

@Slf4j
@Service
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(
            @Qualifier(value = "InMemoryFilmStorage") FilmStorage filmStorage,
            @Qualifier(value = "InMemoryUserStorage") UserStorage userStorage
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film getFilmById(Long id) {
        checkFilmById(id);
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        checkDataRelease(film.getReleaseDate());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        checkDataRelease(film.getReleaseDate());
        checkFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    public void addLike(Long id, Long userId) {
        checkFilmById(id);
        checkUserById(userId);
        filmStorage.addLike(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        checkFilmById(id);
        checkUserById(userId);
        filmStorage.removeLike(id, userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    void checkDataRelease(LocalDate date) {
        if (date == null) {
            log.warn(NULL_DATE);
            throw new NullPointerException(NULL_DATE);
        }
        if (date.isBefore(MIN_DATE_RELEASE)) {
            log.warn("Указана некоректная дата резила");
            throw new ValidationException(String.format(INCORRECT_RELEASE_DATE,
                    MIN_DATE_RELEASE.getDayOfMonth(),
                    MIN_DATE_RELEASE.getYear()));
        }
    }

    void checkFilmById(Long id) {
        if (id == null) {
            log.warn(NULL_ID);
            throw new NullPointerException(NULL_ID);
        }
        if (!filmStorage.getFilms().containsKey(id)) {
            log.warn(String.format(FILM_NOT_FOUND, id));
            throw new NotFoundException(String.format(FILM_NOT_FOUND, id));
        }
    }

    void checkUserById(Long userId) {
        if (userId == null) {
            log.warn(NULL_ID);
            throw new NullPointerException(NULL_ID);
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            log.warn(String.format(USER_NOT_FOUND, userId));
            throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        }
    }
}