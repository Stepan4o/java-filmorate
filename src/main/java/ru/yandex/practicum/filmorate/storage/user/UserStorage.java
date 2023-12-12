package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    List<User> getFriendsList(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User user);

    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);


}
