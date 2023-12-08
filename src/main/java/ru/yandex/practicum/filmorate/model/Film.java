package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.Constant.*;

@Data
@Builder
public class Film {

    private Long id;

    @NotBlank(message = INCORRECT_NAME)
    private final String name;

    @NotNull(message = INCORRECT_DESCRIPTION)
    @Length(max = 200, message = LIMIT_DESCRIPTION)
    private final String description;

    private final LocalDate releaseDate;

    @NotNull(message = INCORRECT_DURATION)
    @Positive(message = INCORRECT_DURATION)
    private final int duration;

    private Mpa mpa;

    private List<Genre> genres;

    public Map<String, Object> filmToMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("film_name", getName());
        result.put("description", getDescription());
        result.put("release_date", getReleaseDate());
        result.put("duration", getDuration());
        result.put("mpa_id_fk", mpa.getId());

        return result;
    }

}
