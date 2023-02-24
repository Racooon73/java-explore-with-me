package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.enums.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {
    private String description;
    private LocalDateTime created;
    private long event;
    private long id;
    private long requester;
    private ParticipationStatus status;
}
