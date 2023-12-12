package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genreService.GenreDbService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreDbService genreDbService;

    public GenreController(GenreDbService genreDbService) {
        this.genreDbService = genreDbService;
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Запрос GET /genres/{}", id);
        return genreDbService.getGenreById(id);
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("Запрос GET /genres");
        return genreDbService.getAll();
    }
}
