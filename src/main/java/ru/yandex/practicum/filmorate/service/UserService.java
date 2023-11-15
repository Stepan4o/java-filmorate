package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constant.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User createUser(User user) {
        checkLogin(user.getLogin());
        checkEmail(user.getEmail());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.createUser(user);
    }

    public List<User> getUsers() {
        return new ArrayList<>(storage.getUsers().values());
    }

    public User getUserById(long id) {
        checkUserById(id);
        return storage.getUserById(id);
    }

    public User updateUser(User user) {
        checkUserById(user.getId());
        checkLogin(user.getLogin());
        return storage.updateUser(user);
    }

    public User addFriend(long id, long friendId) {
        checkUserById(id);
        checkUserById(friendId);
        return storage.addFriend(id, friendId);
    }

    public User removeFriend(long id, long friendId) {
        checkUserById(id);
        checkUserById(friendId);
        return storage.removeFriend(id, friendId);
    }

    public List<User> getFriendsList(long id) {
        checkUserById(id);
        return storage.getFriendsList(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {
        checkUserById(id);
        checkUserById(otherId);
        return storage.getCommonFriends(id, otherId);
    }

    void checkLogin(String login) {
        if (login.contains(" ")) {
            log.warn(INCORRECT_LOGIN);
            throw new ValidationException(INCORRECT_LOGIN);
        }
    }

    void checkEmail(String email) {
        List<User> usersLust = new ArrayList<>(storage.getUsers().values());
        List<String> emailsList = new ArrayList<>();
        for (User user : usersLust) {
            emailsList.add(user.getEmail());
        }
        if (emailsList.contains(email)) {
            log.warn(String.format(ALREADY_EXIST_EMAIL, email));
            throw new ValidationException(String.format(ALREADY_EXIST_EMAIL, email));
        }
    }

    void checkUserById(long id) {
        if (!storage.getUsers().containsKey(id)) {
            log.warn(String.format(USER_NOT_FOUND, id));
            throw new NotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }

}
