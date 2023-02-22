package ru.practicum.service;

import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.NewEventDto;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.FilterEnum;
import ru.practicum.model.requests.EventRequestStatusUpdateRequest;
import ru.practicum.model.requests.EventRequestStatusUpdateResult;
import ru.practicum.model.requests.UpdateEventAdminRequest;
import ru.practicum.model.requests.UpdateEventUserRequest;

import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto postEvent(long userId, NewEventDto eventDto);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto patchEvent(UpdateEventUserRequest event, long userId, long eventId);

    EventRequestStatusUpdateResult patchEventReq(EventRequestStatusUpdateRequest request, long userId, long eventId);

    EventFullDto patchEventAdmin(UpdateEventAdminRequest event, long eventId);

    List<EventFullDto> getEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size);

    List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, String rangeEnd, String rangeStart, Boolean onlyAvailable, FilterEnum sort, int from, int size);

    EventFullDto getEventPuplic(long id);
}
