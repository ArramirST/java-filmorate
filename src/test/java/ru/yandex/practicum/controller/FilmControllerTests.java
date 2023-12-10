package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTests {

    static Film film;
    static FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        film = new Film(1, "Титаник", "Романтика",
                LocalDate.of(1998, 2, 20), 2);
        filmController = new FilmController();
    }

    @Test
    public void shouldPostFIlm() {
        assertEquals(film, filmController.createFilm(film), "Фильм не был добавлен");
    }

    @Test
    public void shouldPutFIlm() {
        filmController.createFilm(film);
        film.setName("Титаник 2");
        assertEquals(film, filmController.updateFilm(film), "Фильм не был обновлен");
    }

    @Test
    public void shouldNotValidateEmptyName() {
        film.setName("");
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("Название не может быть пустым", e.getMessage(),
                "Некорректная обработка фильма с пустым названием");
    }

    @Test
    public void shouldNotValidate201Length() {
        film.setDescription("В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней"
                + " палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти"
                + " замуж по расчёту.  ");
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("Максимальная длина описания — 200 символов", e.getMessage(),
                "Некорректная обработка фильма с длинным описанием");
    }

    @Test
    public void shouldNotValidateEarlyDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", e.getMessage(),
                "Некорректная обработка фильма с неправильной датой");
    }

    @Test
    public void shouldNotValidateNegativeDuration() {
        film.setDuration(-2);
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("Продолжительность фильма должна быть положительной", e.getMessage(),
                "Некорректная обработка фильма с отрицательной длительностью");
    }
}
