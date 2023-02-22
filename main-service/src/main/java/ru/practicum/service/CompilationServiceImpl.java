package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.model.dto.UpdateCompilationDto;
import ru.practicum.model.requests.UpdateCompilationRequest;
import ru.practicum.storage.CategoryRepository;
import ru.practicum.storage.CompilationRepository;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.UserRepository;
import ru.practicum.util.CompilationMapper;
import ru.practicum.util.CustomMapper;
import ru.practicum.util.EventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CustomMapper mapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto dto) {

        Set<Event> eventList = eventRepository.findAllByIdIn(dto.getEvents());
        List<EventShortDto> eventsShort = eventList.stream().map(l -> EventMapper.toShortFromEvent(l,
                        userRepository.findByIdShort(l.getInitiator()),
                        categoryRepository.findById(l.getCategory()), 0, 0))
                .collect(Collectors.toList());
        //TO DO
        return CompilationMapper.toDtoFromCompilation(compilationRepository.save(Compilation.builder()
                .events(eventList)
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .build()), eventsShort);
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto patchCompilation(long compId, UpdateCompilationRequest dto) {

        Optional<Compilation> oCompilation = compilationRepository.findById(compId);

        if (oCompilation.isEmpty()) {
            throw new NotFoundException();
        }
        Compilation compilation = oCompilation.get();

        Set<Event> events = eventRepository.findAllByIdIn(dto.getEvents());

        UpdateCompilationDto compilationDto = UpdateCompilationDto.builder()
                .id(compId)
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .events(events)
                .build();
        mapper.updateCompFromDto(compilationDto, compilation);
        compilation.setEvents(events);


        List<EventShortDto> eventsShort = compilation.getEvents().stream().map(l -> EventMapper.toShortFromEvent(l,
                        userRepository.findByIdShort(l.getInitiator()),
                        categoryRepository.findById(l.getCategory()), 0, 0))
                .collect(Collectors.toList());

        return CompilationMapper.toDtoFromCompilation(compilationRepository.save(Compilation.builder()
                .events(compilation.getEvents())
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .build()), eventsShort);
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, pageRequest);
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<EventShortDto> eventsShort = compilation.getEvents().stream().map(l -> EventMapper.toShortFromEvent(l,
                            userRepository.findByIdShort(l.getInitiator()),
                            categoryRepository.findById(l.getCategory()), 0, 0))
                    .collect(Collectors.toList());
            result.add(CompilationMapper.toDtoFromCompilation(Compilation.builder()
                    .id(compilation.getId())
                    .events(compilation.getEvents())
                    .title(compilation.getTitle())
                    .pinned(compilation.isPinned())
                    .build(), eventsShort));

        }
        return result;
    }

    @Override
    public CompilationDto getCompilation(long compId) {

        Optional<Compilation> oComp = compilationRepository.findById(compId);
        if (oComp.isEmpty()) {
            throw new NotFoundException();
        }
        Compilation dto = oComp.get();

        List<EventShortDto> eventsShort = dto.getEvents().stream().map(l -> EventMapper.toShortFromEvent(l,
                        userRepository.findByIdShort(l.getInitiator()),
                        categoryRepository.findById(l.getCategory()), 0, 0))
                .collect(Collectors.toList());

        return CompilationMapper.toDtoFromCompilation(Compilation.builder()
                .id(compId)
                .events(dto.getEvents())
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .build(), eventsShort);
    }
}
