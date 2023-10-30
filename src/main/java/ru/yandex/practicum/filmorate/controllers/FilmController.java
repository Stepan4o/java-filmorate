package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmModelException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Controller
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private static final LocalDate MIN_DATA_RELEASE = LocalDate.of(1895,12,28);
    private static final short DESCRIPTION_LIMIT = 200;

    private final Map<Integer,Film> films = new HashMap<>();
    private int filmIdCounter = 0;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Запрос на добавление фильма");
        if (film.getReleaseDate().isBefore(MIN_DATA_RELEASE)) {
            log.warn("Фильм не добавлен, поле 'releaseDate' указано некорректно");
            throw new InvalidFilmModelException(InvalidFilmModelException.INCORRECT_RELEASE_DATE);

        } else if (film.getDescription().length() > DESCRIPTION_LIMIT) {
            log.warn("Фильм не добавлен, поле 'description' указано некорректно");
            throw new InvalidFilmModelException(InvalidFilmModelException.LIMIT_DESCRIPTION);

        } else if (film.getDuration() <= 0) {
            log.warn("Фильм не добавлен, поле 'duration' указано некорректно");
            throw new InvalidFilmModelException(InvalidFilmModelException.INCORRECT_DURATION);

        } else if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.warn("Фильм не добавлен, поле 'name' указано некорректно");
            throw new InvalidFilmModelException(InvalidFilmModelException.INCORRECT_NAME);
        }
        increaseId();
        film.setId(filmIdCounter);
        films.put(film.getId(), film);
        log.info("Фильм {}, с id {} создан", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Запрос на обновление информации о фильме");
        if (!films.containsKey(film.getId())) {
            log.warn("Информация о фильме не была обновлена, указан некорректный id");
            throw new InvalidFilmModelException(InvalidFilmModelException.INCORRECT_ID);
        }
        films.put(film.getId(),film);
        log.info("Информация о фильме с id {} успешно обновлена", film.getId());
        return film;
    }

    private void increaseId() {
        filmIdCounter++;
    }
}
