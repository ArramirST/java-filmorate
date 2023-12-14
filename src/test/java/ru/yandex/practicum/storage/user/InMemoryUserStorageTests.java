package ru.yandex.practicum.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryUserStorageTests {

    static User user;
    static InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "mail@yandex.ru", "doloreUpdate", "est adipisicing",
                LocalDate.of(2000, 1, 1));
        inMemoryUserStorage = new InMemoryUserStorage();
    }

    @Test
    public void shouldPostUser() {
        assertEquals(user, inMemoryUserStorage.createUser(user), "Пользователь не был добавлен");
    }

    @Test
    public void shouldPutUser() {
        inMemoryUserStorage.createUser(user);
        user.setName("fera hrrt");
        assertEquals(user, inMemoryUserStorage.updateUser(user), "Пользователь не был обновлен");
    }

    @Test
    public void shouldNotValidateWrongEmail() {
        user.setEmail("nfweifhw");
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> inMemoryUserStorage.createUser(user)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", e.getMessage(),
                "Некорректная обработка пользователя с неверной почтой");
        user.setEmail("");
        e = assertThrows(
                ValidationException.class,
                () -> inMemoryUserStorage.createUser(user)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", e.getMessage(),
                "Некорректная обработка пользователя с неверной почтой");
    }

    @Test
    public void shouldNotValidateWrongLogin() {
        user.setLogin("");
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> inMemoryUserStorage.createUser(user)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage(),
                "Некорректная обработка пользователя с неверным логином");
        user.setLogin("merppm wefjio");
        e = assertThrows(
                ValidationException.class,
                () -> inMemoryUserStorage.createUser(user)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage(),
                "Некорректная обработка пользователя с неверным логином");
    }

    @Test
    public void shouldNotValidateFutureDate() {
        user.setBirthday(LocalDate.of(2100, 1, 1));
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> inMemoryUserStorage.createUser(user)
        );
        assertEquals("Дата рождения не может быть в будущем", e.getMessage(),
                "Некорректная обработка пользователя с неверной датой");
    }
}
