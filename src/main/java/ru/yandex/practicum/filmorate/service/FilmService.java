package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
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

    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public void addLike(long id) throws ObjectNotFoundException {
        Map<Integer, Film> films = filmStorage.getFilms();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        }
        Film film = films.get(id);
        film.addLike(id);
        filmStorage.updateFilm(film);
    }

    public void removeLike(long id) throws ObjectNotFoundException {
        Map<Integer, Film> films = filmStorage.getFilms();
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не существует");
        }
        Film film = films.get(id);
        film.removeLike(id);
        filmStorage.updateFilm(film);
    }

    public Set<Film> showTop10Films() {
        Map<Integer, Film> films = filmStorage.getFilms();
        TreeSet<Film> sortedFilms = new TreeSet<>(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o1.getLikes().size() - o2.getLikes().size();
            }
        });
        sortedFilms.addAll(films.values());
        Set<Film> top10Films = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            top10Films.add(sortedFilms.first());
            sortedFilms.remove(sortedFilms.first());
        }
        return top10Films;
    }
}
