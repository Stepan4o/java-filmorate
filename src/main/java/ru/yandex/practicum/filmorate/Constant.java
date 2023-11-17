package ru.yandex.practicum.filmorate;

import java.time.LocalDate;

public class Constant {

    public static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    public static final String INCORRECT_NAME = "Поле -> 'name' не может быть пустым.";

    public static final String NULL_ID = "Индентификатор -> 'id' не может быть пустым";

    public static final String NULL_DATE = "Дата релиза -> 'releaseDate' не может быть пустым.";

    public static final String INCORRECT_DESCRIPTION = "Описание -> 'description' не может быть пустым.";

    public static final String LIMIT_DESCRIPTION = "Превышен лимит символов в описании фильма. Лимит -> 200";

    public static final String INCORRECT_DURATION = "Продолжительность -> 'duration' должно быть положительным числом.";

    public static final String INCORRECT_EMAIL = "Введите корректную почту -> 'email'.";

    public static final String ALREADY_EXIST_EMAIL = "Email '%s' занят";

    public static final String ALREADY_EXIST_LOGIN = "login '%s' занят";

    public static final String INCORRECT_LOGIN = "Логин не может быть пустым " +
            "или содержать пробелы";

    public static final String INCORRECT_BIRTHDAY = "Дата рождения не может быть указана в будущем " +
            "или оставаться пустым.";

    public static final String FILM_NOT_FOUND = "Фильм с id %d не найден";

    public static final String USER_NOT_FOUND = "Пользователь с id %d не найден";

    public static final String INCORRECT_RELEASE_DATE = "Дата релиза не может быть раньше чем %d декабря %d года.";


}

