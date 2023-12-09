package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @PostMapping("/film")
    public Film createFilm(@RequestBody Film film) {
        films.add(film);
        return film;
    }

    @PutMapping("/film")
    public Film updateFilm(@RequestBody Film film) {
        for (Film filmInMemory : films) {
            if (film.getId()==filmInMemory.getId()) {
                films.remove(filmInMemory);
            }
        }
        films.add(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return films;
    }
}
