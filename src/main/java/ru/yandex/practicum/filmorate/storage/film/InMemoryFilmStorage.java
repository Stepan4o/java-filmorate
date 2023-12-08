package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Фильм с id {} получен", id);
        return getFilms().get(id);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Фильм с id {} создан", film.getId());
        return films.get(film.getId());
    }

    @Override
    public void addLike(Long id, Long userId) {
//        getFilms().get(id).getLikes().add(userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, id);
//        return getFilms().get(id);
    }

    @Override
    public void removeLike(Long id, Long userId) {
//        getFilms().get(id).getLikes().remove(userId);
        log.info("Пользователь с id {} удалил лайк у фильма с id {}", userId, id);
        getFilms().get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм с id {} обновлен", film.getId());
        return films.get(film.getId());
    }

    @Override
    public List<Film> getPopular(int count) {
        List<Film> filmsList = new ArrayList<>(films.values());

//        return filmsList.stream()
//                .sorted(Comparator.comparingInt(t -> -(t.getLikes().size())))
//                .limit(count)
//                .collect(Collectors.toList());
        return List.of();
    }

    @Override
    public List<Film> getAll() {
        return null;
    }
}
