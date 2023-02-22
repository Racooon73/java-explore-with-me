package ru.practicum.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;
    private boolean pinned;
    private String title;
}
