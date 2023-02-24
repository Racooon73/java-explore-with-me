package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.UserShortDto;

import java.util.Optional;

@UtilityClass
public class EventMapper {
    public static EventFullDto toFullFromEvent(Event event, UserShortDto initiator, Optional<Category> category) {
        if (category.isEmpty()) {
            return EventFullDto.builder()
                    .eventDate(event.getEventDate())
                    .id(event.getId())
                    .title(event.getTitle())
                    .annotation(event.getAnnotation())
                    .location(new Location(event.getLat(), event.getLon()))
                    .paid(event.getPaid())
                    .createdOn(event.getCreatedOn())
                    .publishedOn(event.getPublishedOn())
                    .state(event.getState())
                    .description(event.getDescription())
                    .requestModeration(event.getRequestModeration())
                    .participantLimit(event.getParticipantLimit())
                    .category(null)
                    .eventDate(event.getEventDate())
                    .initiator(initiator)
                    .views(0)
                    .build();
        } else {
            return EventFullDto.builder()
                    .eventDate(event.getEventDate())
                    .id(event.getId())
                    .title(event.getTitle())
                    .annotation(event.getAnnotation())
                    .location(new Location(event.getLat(), event.getLon()))
                    .paid(event.getPaid())
                    .createdOn(event.getCreatedOn())
                    .publishedOn(event.getPublishedOn())
                    .state(event.getState())
                    .description(event.getDescription())
                    .requestModeration(event.getRequestModeration())
                    .participantLimit(event.getParticipantLimit())
                    .category(category.get())
                    .eventDate(event.getEventDate())
                    .initiator(initiator)
                    .views(0)
                    .build();
        }
    }

    public static EventShortDto toShortFromEvent(Event event,
                                                 UserShortDto initiator,
                                                 Optional<Category> category,
                                                 long views,
                                                 int confirmedRequests) {
        if (category.isEmpty()) {
            return EventShortDto.builder()
                    .id(event.getId())
                    .confirmedRequests(confirmedRequests)
                    .annotation(event.getAnnotation())
                    .title(event.getTitle())
                    .views(views)
                    .paid(event.getPaid())
                    .category(null)
                    .eventDate(event.getEventDate())
                    .initiator(initiator)
                    .build();
        } else {
            return EventShortDto.builder()
                    .id(event.getId())
                    .confirmedRequests(confirmedRequests)
                    .annotation(event.getAnnotation())
                    .title(event.getTitle())
                    .views(views)
                    .paid(event.getPaid())
                    .category(category.get())
                    .eventDate(event.getEventDate())
                    .initiator(initiator)
                    .build();
        }
    }
}
