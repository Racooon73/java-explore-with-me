package ru.practicum.service;

import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.model.requests.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto dto);

    void deleteCompilation(long compId);

    CompilationDto patchCompilation(long compId, UpdateCompilationRequest compilation);

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilation(long compId);
}
