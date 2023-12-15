package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {


    private Map<Long, Film> films = new HashMap<>();
    private static long minAvailableId = 1;
    private FilmValidation filmValidation;

    @Autowired
    public void setFilmValidation(FilmValidation filmValidation) {
        this.filmValidation = filmValidation;
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        film = generateId(film);
        filmValidation.validationCheck(film, "POST");
        films.put(film.getId(), film);
        log.info("Получен запрос POST к эндпоинту /film, Строка параметров запроса: '{}'", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ObjectNotFoundException {
        film = generateId(film);
        filmValidation.validationCheck(film, "PUT");
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Получен запрос PUT к эндпоинту /film, Строка параметров запроса: '{}'", film);
            return film;
        } else {
            log.warn("Фильма не существует");
            throw new ObjectNotFoundException("Фильма не существует");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    private Film generateId(Film film) {
        if (film.getId() == 0) {
            while (films.containsKey(minAvailableId)) {
                minAvailableId++;
            }
            film.setId(minAvailableId);
        }
        return film;
    }
}
