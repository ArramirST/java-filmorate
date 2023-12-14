package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private static int minAvailableId = 1;

    @Override
    public User createUser(User user) throws ValidationException {
        user = generateIdAndName(user);
        validationCheck(user, "POST");
        users.put(user.getId(), user);
        log.info("Получен запрос POST к эндпоинту /user, Строка параметров запроса: '{}'", user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        user = generateIdAndName(user);
        validationCheck(user, "PUT");
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Получен запрос PUT к эндпоинту /user, Строка параметров запроса: '{}'", user);
            return user;
        } else {
            log.warn("Пользователя не существует");
            throw new ValidationException("Пользователя не существует");
        }
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    private void validationCheck(User user, String request) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Получен запрос {} к эндпоинту /user, ошибка: '{}'", request,
                    "Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Получен запрос {} к эндпоинту /user, ошибка: '{}'", request,
                    "Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Получен запрос {} к эндпоинту /user, ошибка: '{}'", request,
                    "Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private User generateIdAndName(User user) {
        if (user.getId() == 0) {
            while (users.containsKey(minAvailableId)) {
                minAvailableId++;
            }
            user.setId(minAvailableId);
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
