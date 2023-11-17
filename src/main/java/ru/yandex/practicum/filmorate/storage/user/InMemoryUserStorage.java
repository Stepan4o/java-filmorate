package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public List<User> getFriendsList(Long id) {
        List<Long> friendsIds = new ArrayList<>(getUsers().get(id).getFriends());
        List<User> friendsList = new ArrayList<>();
        for (Long userId : friendsIds) {
            User friend = getUserById(userId);
            friendsList.add(friend);
        }
        log.info("Список друзей пользователя с id {} получен", id);
        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        List<Long> friendsIds = new ArrayList<>(getUsers().get(id).getFriends());
        User otherUser = getUsers().get(otherId);
        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : otherUser.getFriends()) {
            if (friendsIds.contains(friendId)) {
                commonFriends.add(getUsers().get(friendId));
            }
        }
        log.info("Список общих друзей пользователей с id {} и {} получен", id, otherId);
        return commonFriends;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь с id {} создан.", user.getId());
        return users.put(user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь с id {} обновлён.", user.getId());
        return users.get(user.getId());
    }

    @Override
    public User addFriend(Long id, Long friendId) {
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        log.info("Пользователи с id {} и {} теперь друзья", id, friendId);
        return users.get(id);
    }

    @Override
    public User removeFriend(Long id, Long friendId) {
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
        log.info("Пользователи с id {} и {} больше не друзья", id, friendId);
        return users.get(id);
    }
}
