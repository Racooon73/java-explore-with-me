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
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private long views;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private EventState state;
    private Boolean requestModeration;

}
