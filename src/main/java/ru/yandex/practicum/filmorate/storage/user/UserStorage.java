package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> getUsers();

    List<User> getFriendsList(long id);

    List<User> getCommonFriends(long id, long otherId);

    User getUserById(long id);

    User createUser(User user);

    User updateUser(User user);

    User addFriend(long id, long friendId);

    User removeFriend(long id, long friendId);


}
