package ru.yandex.practicum.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryFilmStorageTests {

    static Film film;
    static InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    public void beforeEach() {
        film = new Film(1, "Титаник", "Романтика",
                LocalDate.of(1998, 2, 20), 2);
        inMemoryFilmStorage = new InMemoryFilmStorage(new FilmValidation());
    }

    @Test
    public void shouldPostFIlm() {
        assertEquals(film, inMemoryFilmStorage.createFilm(film), "Фильм не был добавлен");
    }

    @Test
    public void shouldPutFIlm() {
        inMemoryFilmStorage.createFilm(film);
        film.setName("Титаник 2");
        assertEquals(film, inMemoryFilmStorage.updateFilm(film), "Фильм не был обновлен");
    }

    @Test
    public void shouldNotValidateEmptyName() {
        film.setName("");
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> inMemoryFilmStorage.createFilm(film)
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
                () -> inMemoryFilmStorage.createFilm(film)
        );
        assertEquals("Максимальная длина описания — 200 символов", e.getMessage(),
                "Некорректная обработка фильма с длинным описанием");
    }

    @Test
    public void shouldNotValidateEarlyDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> inMemoryFilmStorage.createFilm(film)
        );
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", e.getMessage(),
                "Некорректная обработка фильма с неправильной датой");
    }

    @Test
    public void shouldNotValidateNegativeDuration() {
        film.setDuration(-2);
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> inMemoryFilmStorage.createFilm(film)
        );
        assertEquals("Продолжительность фильма должна быть положительной", e.getMessage(),
                "Некорректная обработка фильма с отрицательной длительностью");
    }
}
