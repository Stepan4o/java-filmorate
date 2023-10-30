package ru.yandex.practicum.filmorate.exceptions;

public class InvalidUserModelException extends RuntimeException{

    public static final String INCORRECT_BIRTHDAY = "Дата рождения не может быть в будущем.";
    public static final String INCORRECT_EMAIL = "Email не может быть пустым и должен содержать знак '@'";
    public static final String INCORRECT_LOGIN = "Логин не может быть пустым или содержать пробелы";
    public static final String INCORRECT_ID = "Не существует пользователя с id -> ";

    public InvalidUserModelException(String message) {
        super(message);
    }

}
