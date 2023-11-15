package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constant.*;

@Data
@Builder
public class Film {

    private long id;

    @NotBlank(message = INCORRECT_NAME)
    private final String name;

    @NotNull(message = INCORRECT_DESCRIPTION)
    @Length(max = 200, message = LIMIT_DESCRIPTION)
    private final String description;

    private final LocalDate releaseDate;

    @NotNull(message = INCORRECT_DURATION)
    @Positive(message = INCORRECT_DURATION)
    private final int duration;

    private final Set<Long> likes = new HashSet<>();

}
