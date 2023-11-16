package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constant.*;

@Data
@Builder
public class User {

    private Long id;

    @Email(message = INCORRECT_EMAIL)
    private final String email;

    @NotBlank(message = INCORRECT_LOGIN)
    private final String login;

    private String name;

    @NotNull(message = INCORRECT_BIRTHDAY)
    @PastOrPresent(message = INCORRECT_BIRTHDAY)
    private final LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

}
