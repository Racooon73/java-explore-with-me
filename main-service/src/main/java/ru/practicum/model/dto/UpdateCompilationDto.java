package ru.practicum.model.dto;

import lombok.*;
import ru.practicum.model.Event;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompilationDto {
    private long id;
    private Set<Event> events;
    private Boolean pinned;
    private String title;
}
