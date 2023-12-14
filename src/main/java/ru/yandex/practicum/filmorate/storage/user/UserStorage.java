package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User createUser(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException;
    List<User> findAllUsers();
    public Map<Integer, User> getUsers();
}
