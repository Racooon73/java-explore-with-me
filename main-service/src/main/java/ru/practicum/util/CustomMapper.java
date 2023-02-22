package ru.practicum.util;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.dto.UpdateCompilationDto;
import ru.practicum.model.requests.UpdateEventAdminRequest;
import ru.practicum.model.requests.UpdateEventUserRequest;

@Mapper(componentModel = "spring")
public interface CustomMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromDto(UpdateEventUserRequest dto, @MappingTarget Event entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromAdminDto(UpdateEventAdminRequest dto, @MappingTarget Event entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompFromDto(UpdateCompilationDto dto, @MappingTarget Compilation entity);
}
