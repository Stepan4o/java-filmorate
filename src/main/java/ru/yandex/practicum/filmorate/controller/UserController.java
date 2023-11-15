package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("Запрос POST /users");
        return userService.createUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Запрос GET /users");
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable long id) {
        log.info("Запрос GET /users/{}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.info("Запрос PUT /users");
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Запрос PUT /users/{}/friends/{}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Запрос DELETE /users/{}/friends/{}", id, friendId);
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsList(@PathVariable long id) {
        log.info("Запрос GET /users/{}/friends", id);
        return userService.getFriendsList(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Запрос GET /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

}