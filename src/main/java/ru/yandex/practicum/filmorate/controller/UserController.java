package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private List<User> users = new ArrayList<>();

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        user = generateIdAndName(user);
        validationCheck(user, "POST");
        users.add(user);
        log.info("Получен запрос POST к эндпоинту /user, Строка параметров запроса: '{}'", user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        validationCheck(user, "PUT");
        boolean isExist = false;
        User sameUser = null;
        for (User userInMemory : users) {
            if (user.getId() == userInMemory.getId()) {
                isExist = true;
                sameUser = userInMemory;
            }
        }
        users.remove(sameUser);
        if (isExist) {
            users.add(user);
            log.info("Получен запрос PUT к эндпоинту /user, Строка параметров запроса: '{}'", user);
            return user;
        } else {
            log.warn("Пользователя не существует");
            throw new ValidationException("Пользователя не существует");
        }
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
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
            for (int i = 1; i < 10000; i++) {
                boolean isExist = false;
                for (User user1 : users) {
                    if (user1.getId() == i) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    user.setId(i);
                    break;
                }
            }
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
