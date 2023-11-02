package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmModelException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private int id;

    @NotBlank(message = InvalidFilmModelException.INCORRECT_NAME)
    private final String name;

    @NotNull(message = InvalidFilmModelException.LIMIT_DESCRIPTION)
    @Length(max = 200, message = InvalidFilmModelException.LIMIT_DESCRIPTION)
    private final String description;

    private final LocalDate releaseDate;

    @NotNull(message = InvalidFilmModelException.INCORRECT_DURATION)
    @Positive(message = InvalidFilmModelException.INCORRECT_DURATION)
    private final int duration;

}
