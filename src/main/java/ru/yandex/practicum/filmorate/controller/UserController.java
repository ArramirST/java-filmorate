package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private List<User> users = new ArrayList<>();

    @PostMapping("/user")
    public User createFilm(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @PutMapping("/user")
    public User updateFilm(@RequestBody User user) {
        for (User userInMemory : users) {
            if (user.getId()==userInMemory.getId()) {
                users.remove(userInMemory);
            }
        }
        users.add(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> findAllFilms() {
        return users;
    }
}
