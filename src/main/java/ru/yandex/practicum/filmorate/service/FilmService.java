package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {

    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) throws ValidationException {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) throws ObjectNotFoundException {
        return filmStorage.updateFilm(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film addLike(long id, long userId) throws ObjectNotFoundException {
        Map<Long, Film> films = filmStorage.getFilms();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        } else if (userId <= 0) {
            throw new ObjectNotFoundException("Пользователя не существет");
        }
        Film film = films.get(id);
        film.addLike(userId);
        return filmStorage.updateFilm(film);
    }

    public Film removeLike(long id, long userId) throws ObjectNotFoundException {
        Map<Long, Film> films = filmStorage.getFilms();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        } else if (userId <= 0) {
            throw new ObjectNotFoundException("Пользователя не существет");
        }
        Film film = films.get(id);
        film.removeLike(userId);
        return filmStorage.updateFilm(film);
    }

    public Set<Film> showTopFilms(int count) {
        Map<Long, Film> films = filmStorage.getFilms();
        TreeSet<Film> sortedFilms = new TreeSet<>((o1, o2) -> {
            if (o2.getLikes().size() == o1.getLikes().size()) {
                return ((int) o2.getId() - (int) o1.getId());
            }
            return o2.getLikes().size() - o1.getLikes().size();
        });
        sortedFilms.addAll(films.values());
        Set<Film> topFilms = new HashSet<>();
        if (sortedFilms.size() < count) {
            count = sortedFilms.size();
        }
        for (int i = 0; i <= (count-1); i++) {
            topFilms.add(sortedFilms.first());
            sortedFilms.remove(sortedFilms.first());
        }
        return topFilms;
    }

    public Film getFilm(long id) throws ObjectNotFoundException {
        Map<Long, Film> films = filmStorage.getFilms();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        }
        return films.get(id);
    }
}
