package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidUserModelException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Integer, User> users = new HashMap<>();
    private final LocalDate NOW = LocalDate.now();
    private int userIdCounter = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
    log.info("Запрос на создание пользователя.");
     if (user.getEmail().isBlank()
             || user.getEmail().isEmpty()
             || !user.getEmail().contains("@")) {
         log.warn("Пользователь не создан, поле email указано некорректно");
         throw new InvalidUserModelException(InvalidUserModelException.INCORRECT_EMAIL);

        } else if (user.getLogin().isBlank()
             || user.getLogin().contains(" ")
             || user.getLogin().isEmpty()) {
         log.warn("Пользователь не создан, поле login указано некорректно");
         throw new InvalidUserModelException(InvalidUserModelException.INCORRECT_LOGIN);

        } else if (user.getBirthday().isAfter(NOW)) {
         log.warn("Пользователь не создан, поле birthday указано некорректно");
         throw new InvalidUserModelException(InvalidUserModelException.INCORRECT_BIRTHDAY);

     }
     increaseId();
     user.setId(userIdCounter);
     if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
         user.setName(user.getLogin());
     }
     users.put(user.getId(), user);
     log.info("Пользователь с id " + user.getId() + " создан.");
     return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Запрос на обновления пользователя");
        if (!users.containsKey(user.getId())) {
            log.warn("Информация о пользователе не была обновлена, указан некорректный id");
            throw new InvalidUserModelException(InvalidUserModelException.INCORRECT_ID + user.getId());
        }
        log.info("Пользователь с id {} обновлён.", user.getId());
        users.put(user.getId(), user);
        return user;
    }
    private void increaseId() {
        userIdCounter++;
    }

}