package ru.yandex.practicum.filmorate.service.userService;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceInterface {

    User addUser(User user);

    User getUserById(Long id);

    List<User> getAll();

    User updateUser(User user);

    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);

    List<User> getFriendsList(Long id);

    List<User> getCommonFriends(Long id, Long otherId);


}
