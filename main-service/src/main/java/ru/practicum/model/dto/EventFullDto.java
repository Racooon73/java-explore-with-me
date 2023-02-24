package ru.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.Category;
import ru.practicum.model.Location;
import ru.practicum.model.enums.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    @JsonFormat
    private String annotation;
    @JsonFormat
    private Category category;
    @JsonFormat
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat
    private String description;
    @JsonFormat
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat
    private Long id;
    @JsonFormat
    private UserShortDto initiator;
    @JsonFormat
    private long views;
    @JsonFormat
    private Location location;
    @JsonFormat
    private Boolean paid;
    @JsonFormat
    private Integer participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @JsonFormat
    private EventState state;
    @JsonFormat
    private Boolean requestModeration;

}
