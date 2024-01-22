package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    @Email
    private String email;
    @NotBlank @NotNull
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>(); //todo

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(long id) {
        friends.add(id);
    }

    public void removeFriend(long friendId) {
        friends.remove(friendId);
    }
}
