package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        film = generateId(film);
        validationCheck(film, "POST");
        films.add(film);
        log.info("Получен запрос POST к эндпоинту /film, Строка параметров запроса: '{}'", film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        film = generateId(film);
        validationCheck(film, "PUT");
        Film sameFilm = null;
        boolean isExist = false;
        for (Film filmInMemory : films) {
            if (film.getId() == filmInMemory.getId()) {
                isExist = true;
                sameFilm = filmInMemory;
            }
        }
        films.remove(sameFilm);
        if (isExist) {
            films.add(film);
            log.info("Получен запрос PUT к эндпоинту /film, Строка параметров запроса: '{}'", film);
            return film;
        } else {
            log.warn("Фильма не существует");
            throw new ValidationException("Фильма не существует");
        }
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return films;
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

    private Film generateId(Film film) {
        if (film.getId() == 0) {
            for (int i = 1; i < 10000; i++) {
                boolean isExist = false;
                for (Film film1 : films) {
                    if (film1.getId() == i) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    film.setId(i);
                    break;
                }
            }
        }
        return film;
    }
}
