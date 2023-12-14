package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {


    private Map<Integer, Film> films = new HashMap<>();
    private static int minAvailableId = 1;


    @Override
    public Film createFilm(Film film) throws ValidationException {
        film = generateId(film);
        validationCheck(film, "POST");
        films.put(film.getId(), film);
        log.info("Получен запрос POST к эндпоинту /film, Строка параметров запроса: '{}'", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        film = generateId(film);
        validationCheck(film, "PUT");
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Получен запрос PUT к эндпоинту /film, Строка параметров запроса: '{}'", film);
            return film;
        } else {
            log.warn("Фильма не существует");
            throw new ValidationException("Фильма не существует");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void validationCheck(Film film, String request) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.warn("Получен запрос {} к эндпоинту /film, ошибка: '{}'", request,
                    "Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Получен запрос {} к эндпоинту /film, ошибка: '{}'", request,
                    "Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Получен запрос {} к эндпоинту /film, ошибка: '{}'", request,
                    "Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.warn("Получен запрос {} к эндпоинту /film, ошибка: '{}'", request,
                    "Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    @Override
    public Map<Integer, Film> getFilms() {
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
