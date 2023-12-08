package ru.yandex.practicum.filmorate;

import java.time.LocalDate;

public class Constant {

    public static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    public static final String INCORRECT_NAME =
            "Поле -> 'name' не может быть пустым";

    public static final String NULL_ID =
            "Индентификатор -> 'id' не может быть пустым";

    public static final String NULL_DATE =
            "Дата релиза -> 'releaseDate' не может быть пустым";

    public static final String INCORRECT_DESCRIPTION =
            "Описание -> 'description' не может быть пустым";

    public static final String LIMIT_DESCRIPTION =
            "Превышен лимит символов в описании фильма. Лимит -> 200";

    public static final String INCORRECT_DURATION =
            "Продолжительность -> 'duration' должно быть положительным числом.";

    public static final String INCORRECT_EMAIL =
            "Введите корректную почту -> 'email'";

    public static final String ALREADY_EXIST_EMAIL = "Email '%s' занят";

    public static final String ALREADY_EXIST_LOGIN = "login '%s' занят";

    public static final String INCORRECT_LOGIN = "Логин не может быть пустым " +
            "или содержать пробелы";

    public static final String INCORRECT_BIRTHDAY =
            "Дата рождения не может быть указана в будущем " +
                    "или оставаться пустым.";

    public static final String FILM_NOT_FOUND = "Фильм с id %d не найден";

    public static final String USER_NOT_FOUND = "Пользователь с id %d не найден";

    public static final String INCORRECT_RELEASE_DATE =
            "Дата релиза не может быть раньше чем %d декабря %d года.";

    public static final String USER_TABLE = "users";

    public static final String FILM_TABLE = "films";

    public static final String USER_ID = "user_id";

    public static final String EMAIL = "email";

    public static final String FILM_ID = "film_id";

    public static final String DROP_ALL_TABLES =
            "DROP TABLE IF EXISTS FILM_GENRE  CASCADE;" +
                    "DROP TABLE IF EXISTS FILMS  CASCADE;" +
                    "DROP TABLE IF EXISTS FRIENDSHIP  CASCADE;" +
                    "DROP TABLE IF EXISTS GENRE  CASCADE;" +
                    "DROP TABLE IF EXISTS LIKES  CASCADE;" +
                    "DROP TABLE IF EXISTS MPA  CASCADE;" +
                    "DROP TABLE IF EXISTS USERS  CASCADE;";

    public static final String CREATE_TABLES_FOR_USER_TEST =
            "CREATE TABLE USERS (" +
                    "user_id int PRIMARY KEY AUTO_INCREMENT, " +
                    "email varchar(255) not null unique, " +
                    "login varchar(255), USER_NAME varchar(255), " +
                    "birthday date); " +
                    "CREATE TABLE FRIENDSHIP (" +
                    "user_id_fk int references USERS(USER_ID), " +
                    "friend_id_fk int references USERS(USER_ID)); ";

    public static final String CREATE_TABLES_FOR_FILMS_TEST =
            "CREATE TABLE FILMS ( " +
                    "film_id int PRIMARY KEY AUTO_INCREMENT, " +
                    "film_name varchar(255) not null, " +
                    "description varchar(200) not null, " +
                    "release_date timestamp not null, " +
                    "duration int not null, " +
                    "mpa_id_fk int not null); " +
                    "CREATE TABLE GENRE ( " +
                    "genre_id int PRIMARY KEY AUTO_INCREMENT, " +
                    "genre_name varchar(255) not null); " +
                    "CREATE TABLE FILM_GENRE ( " +
                    "film_id_fk int not null references FILMS(FILM_ID), " +
                    "genre_id_fk int not null references GENRE(GENRE_ID)); " +
                    "CREATE TABLE MPA ( " +
                    "mpa_id int PRIMARY KEY AUTO_INCREMENT, " +
                    "mpa_name varchar(255) not null); " +
                    "ALTER TABLE FILMS ADD FOREIGN KEY (MPA_ID_FK) REFERENCES MPA(MPA_ID); " +
                    "INSERT INTO MPA " +
                    "(mpa_name) " +
                    "VALUES('G'), ('PG'), ('PG-13'), ('R'), ('NC-17'); " +
                    "INSERT INTO GENRE (genre_name) VALUES ('Комедия'), ('Драма'), " +
                    "('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик') ";

    public static final String CREATE_TABLE_FOR_LIKE_TEST =
            "CREATE TABLE USERS ( " +
                    "user_id int PRIMARY KEY AUTO_INCREMENT, " +
                    "email varchar(255) not null unique, " +
                    "login varchar(255), USER_NAME varchar(255), " +
                    "birthday date); " +
                    "CREATE TABLE LIKES ( " +
                    "user_id_fk int not null references USERS(USER_ID), " +
                    "film_id_fk int not null references FILMS(FILM_ID)); ";

    public static final String CREATE_TABLE_FOR_GENRE_TEST =
            "CREATE TABLE GENRE (" +
                    " genre_id int PRIMARY KEY AUTO_INCREMENT," +
                    " genre_name varchar(255) not null); " +
                    "INSERT INTO GENRE (genre_name) VALUES ('Комедия'), ('Драма'), " +
                    "('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик') ";

    public static final String CREATE_TABLE_FOR_MPA_TEST =
            "CREATE TABLE MPA ( " +
                    "mpa_id int PRIMARY KEY AUTO_INCREMENT, " +
                    "mpa_name varchar(255) not null); " +
                    "INSERT INTO MPA " +
                    "(mpa_name) " +
                    "VALUES('G'), ('PG'), ('PG-13'), ('R'), ('NC-17'); ";

}

