package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Component
public class UserValidation {
    public void validationCheck(User user, String request) throws ValidationException {
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
}
