package ru.yandex.practicum.filmorate.exceptions;

public class InvalidUserModelException extends RuntimeException {

    public static final String INCORRECT_BIRTHDAY = "Пользователь не создан, " +
            "дата рождения не может быть указана в будущем или оставаться пустым.";

    public static final String INCORRECT_EMAIL = "Пользователь не создан, " +
            "поле 'email' заполнено некорректно";

    public static final String INCORRECT_LOGIN = "Логин не может быть пустым " +
            "или содержать пробелы";

    public static final String INCORRECT_ID = "Не существует пользователя с id -> ";

    public InvalidUserModelException(String message) {
        super(message);
    }

}
