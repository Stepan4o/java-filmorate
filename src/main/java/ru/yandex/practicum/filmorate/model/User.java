package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

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

    public Map<String, Object> userToMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("user_id", getId());
        result.put("user_name", getName());
        result.put("email", getEmail());
        result.put("login", getLogin());
        result.put("birthday", getBirthday());

        return result;
    }

}
