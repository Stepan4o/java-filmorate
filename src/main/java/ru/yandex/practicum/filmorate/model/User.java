package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.InvalidUserModelException;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private int id;

    @Email(message = InvalidUserModelException.INCORRECT_EMAIL)
    private final String email;

    private final String login;

    private String name;

    private final LocalDate birthday;

}
