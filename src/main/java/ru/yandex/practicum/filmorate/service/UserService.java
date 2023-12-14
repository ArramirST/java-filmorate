package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public User updateUser( User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public void addFriend(long id, long friendId) throws ObjectNotFoundException {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new ObjectNotFoundException("Пользователя не существует");
        }
        User user = users.get(id);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void removeFriend(long id, long friendId) throws ObjectNotFoundException {
        Map<Integer, User> users = userStorage.getUsers();
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

    public Set<Long> getFriends(long id) throws ObjectNotFoundException {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователя не существует");
        }
        return users.get(id).getFriends();
    }

    public User getUser(long id) throws ObjectNotFoundException {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователя не существует");
        }
        return users.get(id);
    }
}
