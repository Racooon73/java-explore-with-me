package ru.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.Category;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EventShortDto {
    @JsonFormat
    private String annotation;
    @JsonFormat
    private Category category;
    @JsonFormat
    private long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat
    private long id;
    @JsonFormat
    private UserShortDto initiator;
    @JsonFormat
    private Boolean paid;
    @JsonFormat
    private String title;
    @JsonFormat
    private long views;
}
