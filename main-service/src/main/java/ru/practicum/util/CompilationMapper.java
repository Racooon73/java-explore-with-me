package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.model.Compilation;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.EventShortDto;

import java.util.List;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toDtoFromCompilation(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(events)
                .build();
    }
}
