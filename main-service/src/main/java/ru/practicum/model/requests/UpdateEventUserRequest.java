package ru.practicum.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.Location;
import ru.practicum.model.enums.UserStateAction;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEventUserRequest {
    @JsonFormat
    private String annotation;
    @JsonFormat
    private Long category;
    @JsonFormat
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat
    Location location;
    @JsonFormat
    private Boolean paid;
    @JsonFormat
    private Integer participantLimit;
    @JsonFormat
    private Boolean requestModeration;
    @JsonFormat
    private UserStateAction stateAction;
    @JsonFormat
    private String title;
}
