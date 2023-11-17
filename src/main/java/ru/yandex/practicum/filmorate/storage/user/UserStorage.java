package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> getUsers();

    List<User> getFriendsList(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(User user);

    User addFriend(Long id, Long friendId);

    User removeFriend(Long id, Long friendId);


}
