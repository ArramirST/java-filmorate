package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private static long minAvailableId = 1;
    private UserValidation userValidation;

    public InMemoryUserStorage(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    @Override
    public User createUser(User user) throws ValidationException {
        user = generateIdAndName(user);
        userValidation.validationCheck(user, "POST");
        users.put(user.getId(), user);
        log.info("Получен запрос POST к эндпоинту /user, Строка параметров запроса: '{}'", user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ObjectNotFoundException {
        user = generateIdAndName(user);
        userValidation.validationCheck(user, "PUT");
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Получен запрос PUT к эндпоинту /user, Строка параметров запроса: '{}'", user);
            return user;
        } else {
            log.warn("Пользователя не существует");
            throw new ObjectNotFoundException("Пользователя не существует");
        }
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    private User generateIdAndName(User user) {
        if (user.getId() == 0) {
            while (users.containsKey(minAvailableId)) {
                minAvailableId++;
            }
            user.setId(minAvailableId);
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
