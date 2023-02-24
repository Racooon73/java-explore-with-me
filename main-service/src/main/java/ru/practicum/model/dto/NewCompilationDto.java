package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {
    private long id;
    private List<Long> events;
    private boolean pinned;
    @NotNull
    @NotBlank
    private String title;
}
