package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public User getUserById(Long id) {
        checkUserById(id);
        return storage.getUserById(id);
    }

    public User updateUser(User user) {
        checkUserById(user.getId());
        checkEmailToUpdate(user);
        checkLoginToUpdate(user);
        return storage.updateUser(user);
    }

    public User addFriend(Long id, Long friendId) {
        checkUserById(id);
        checkUserById(friendId);
        if (id.equals(friendId)) {
            log.warn("id={} попытка добавить себя в друзья", id);
            throw new ValidationException("Невозможно добавить себя в друзья");
        }
        return storage.addFriend(id, friendId);
    }

    public User removeFriend(Long id, Long friendId) {
        checkUserById(id);
        checkUserById(friendId);
        return storage.removeFriend(id, friendId);
    }

    public List<User> getFriendsList(Long id) {
        checkUserById(id);
        return storage.getFriendsList(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        checkUserById(id);
        checkUserById(otherId);
        return storage.getCommonFriends(id, otherId);
    }

    void checkLogin(String login) {
        if (login.contains(" ")) {
            log.warn(INCORRECT_LOGIN);
            throw new ValidationException(INCORRECT_LOGIN);
        }
        Set<String> loginsList = getUsers().stream()
                .map(User::getLogin)
                .collect(Collectors.toSet());
        if (loginsList.contains(login)) {
            log.warn(String.format(ALREADY_EXIST_LOGIN, login));
            throw new ValidationException(String.format(ALREADY_EXIST_LOGIN, login));
        }
    }

    void checkEmail(String email) {
        if (email == null) {
            log.warn(INCORRECT_EMAIL);
            throw new NullPointerException(INCORRECT_EMAIL);
        }
        Set<String> emailsList = getUsers().stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        if (emailsList.contains(email)) {
            log.warn(String.format(ALREADY_EXIST_EMAIL, email));
            throw new ValidationException(String.format(ALREADY_EXIST_EMAIL, email));
        }
    }

    void checkEmailToUpdate(User user) {
        if (user.getEmail() == null) {
            log.warn(INCORRECT_EMAIL);
            throw new ValidationException(INCORRECT_EMAIL);
        }
        String incomingEmail = user.getEmail();
        String existEmail = getUserById(user.getId()).getEmail();

        if (!incomingEmail.equals(existEmail)) {
            Set<String> emailsList = getUsers().stream()
                    .map(User::getEmail)
                    .collect(Collectors.toSet());

            if (emailsList.contains(incomingEmail)) {
                log.warn(String.format(ALREADY_EXIST_EMAIL, incomingEmail));
                throw new ValidationException(String.format(ALREADY_EXIST_EMAIL, incomingEmail));
            }
        }
    }

    void checkLoginToUpdate(User user) {
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.warn(INCORRECT_LOGIN);
            throw new ValidationException(INCORRECT_LOGIN);
        }
        String incomingLogin = user.getLogin();
        String existLogin = getUserById(user.getId()).getLogin();

        if (!incomingLogin.equals(existLogin)) {
            Set<String> loginsList = getUsers().stream()
                    .map(User::getLogin)
                    .collect(Collectors.toSet());
            if (loginsList.contains(incomingLogin)) {
                log.warn(ALREADY_EXIST_LOGIN);
                throw new ValidationException(ALREADY_EXIST_LOGIN);
            }
        }
    }

    void checkUserById(Long id) {
        if (id == null) {
            throw new ValidationException(NULL_ID);
        }
        if (!storage.getUsers().containsKey(id)) {
            log.warn(String.format(USER_NOT_FOUND, id));
            throw new NotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }

}
