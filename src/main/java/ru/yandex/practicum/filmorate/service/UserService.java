package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws ObjectNotFoundException {
        return userStorage.updateUser(user);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User addFriend(long id, long friendId) throws ObjectNotFoundException {
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new ObjectNotFoundException("Пользователя не существует");
        }
        User user = users.get(id);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        return user;
    }

    public void removeFriend(long id, long friendId) throws ObjectNotFoundException {
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new ObjectNotFoundException("Пользователя не существует");
        }
        User user = users.get(id);
        User friend = users.get(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(id);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public Set<User> getFriends(long id) throws ObjectNotFoundException {
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователя не существует");
        }
        Set<Long> friendsId = users.get(id).getFriends();
        Set<User> friends = new LinkedHashSet<>();
        for (Long friendId : friendsId) {
            friends.add(users.get(friendId));
        }
        return friends;
    }

    public User getUser(long id) throws ObjectNotFoundException {
        Map<Long, User> users = userStorage.getUsers();
        if (!(users.containsKey(id))) {
            throw new ObjectNotFoundException("Пользователя не существует");
        }
        return users.get(id);
    }

    public Set<User> getMutualFriend(long id, long otherId) throws ObjectNotFoundException {
        Set<User> user1Friends = getFriends(id);
        Set<User> user2Friends = getFriends(otherId);
        Set<User> mutualFriends = new HashSet<>();
        for (User user1Friend : user1Friends) {
            if (user2Friends.contains(user1Friend)) {
                mutualFriends.add(user1Friend);
            }
        }
        return mutualFriends;
    }
}
