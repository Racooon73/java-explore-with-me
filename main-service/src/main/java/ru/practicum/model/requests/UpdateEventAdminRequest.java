package ru.practicum.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.Location;
import ru.practicum.model.enums.AdminStateAction;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEventAdminRequest {
    @JsonFormat
    private String annotation;
    @JsonFormat
    private long category;
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
    private AdminStateAction stateAction;
    @JsonFormat
    private String title;
}
