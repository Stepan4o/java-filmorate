package ru.yandex.practicum.filmorate.exceptions;

public class InvalidFilmModelException extends RuntimeException {
    public static final String LIMIT_DESCRIPTION = "Превышен лимит символов в описании фильма.";
    public static final String INCORRECT_RELEASE_DATE = "Дата релиза не может быть раньше чем 28 декабря 1895 года.";
    public static final String INCORRECT_ID = "Передан некорректный id.";
    public static final String INCORRECT_DURATION = "Продолжительность фильм должна быть положительной.";
    public static final String INCORRECT_NAME = "Название фильма не может быть пустым.";

    public InvalidFilmModelException(String message) {
        super(message);
    }
}