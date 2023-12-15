package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(long id, long userId) throws ObjectNotFoundException {
        Map<Long, Film> films = filmStorage.getFilms();
        Map<Long, User> users = userStorage.getUsers();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        } else if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException("Пользователя не существет");
        }
        Film film = films.get(id);
        film.addLike(userId);
        return filmStorage.updateFilm(film);
    }

    public Film removeLike(long id, long userId) throws ObjectNotFoundException {
        Map<Long, Film> films = filmStorage.getFilms();
        Map<Long, User> users = userStorage.getUsers();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        } else if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException("Пользователя не существет");
        }
        Film film = films.get(id);
        film.removeLike(userId);
        return filmStorage.updateFilm(film);
    }

    public List<Film> showTopFilms(int count) {
        Map<Long, Film> films = filmStorage.getFilms();

        List<Film> sortedFilms = films.values().stream().sorted((o1, o2) -> {
            if (o2.getLikes().size() == o1.getLikes().size()) {
                return ((int)o1.getId() - (int)o2.getId());
            }
            return o2.getLikes().size() - o1.getLikes().size();
        }).limit(count).collect(Collectors.toList());
        return sortedFilms;
    }

    public Film getFilm(long id) throws ObjectNotFoundException {
        Map<Long, Film> films = filmStorage.getFilms();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        }
        return films.get(id);
    }
}
