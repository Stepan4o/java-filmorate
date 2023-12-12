package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpaService.MpaDbService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDbService mpaDbService;

    @Autowired
    public MpaController(MpaDbService mpaDbService) {
        this.mpaDbService = mpaDbService;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        log.info("Запрос GET /mpa/{}", id);
        return mpaDbService.getMpaById(id);
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Запрос GET /mpa");
        return mpaDbService.getAll();
    }
}
