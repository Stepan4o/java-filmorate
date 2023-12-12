package ru.yandex.practicum.filmorate.service.userService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service("UserDbService")
public class UserDbService implements UserServiceInterface {

    private final UserStorage userStorage;

    public UserDbService(@Qualifier(value = "UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        userStorage.addFriend(id, friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        userStorage.removeFriend(id, friendId);
    }

    @Override
    public List<User> getFriendsList(Long id) {
        return userStorage.getFriendsList(id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
