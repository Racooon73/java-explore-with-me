package ru.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewEventDto {

    @NotNull
    @NotBlank
    private String annotation;
    private long category;
    @NotNull
    @NotBlank
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    Location location;
    private Boolean paid;
    private Integer participantLimit;
    private boolean requestModeration;
    @NotNull
    @NotBlank
    private String title;
}
