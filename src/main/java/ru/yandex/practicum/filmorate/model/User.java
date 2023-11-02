package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.InvalidUserModelException;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private int id;

    @Email(message = InvalidUserModelException.INCORRECT_EMAIL)
    private final String email;

    @NotBlank(message = InvalidUserModelException.INCORRECT_LOGIN)
    private final String login;

    private String name;

    @NotNull(message = InvalidUserModelException.INCORRECT_BIRTHDAY)
    @PastOrPresent(message = InvalidUserModelException.INCORRECT_BIRTHDAY)
    private final LocalDate birthday;

}
