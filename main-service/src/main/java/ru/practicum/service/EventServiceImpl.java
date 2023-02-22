package ru.practicum.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.model.QEvent;
import ru.practicum.model.User;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.NewEventDto;
import ru.practicum.model.dto.UserShortDto;
import ru.practicum.model.enums.*;
import ru.practicum.model.requests.EventRequestStatusUpdateRequest;
import ru.practicum.model.requests.EventRequestStatusUpdateResult;
import ru.practicum.model.requests.UpdateEventAdminRequest;
import ru.practicum.model.requests.UpdateEventUserRequest;
import ru.practicum.storage.CategoryRepository;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.ParticipationRepository;
import ru.practicum.storage.UserRepository;
import ru.practicum.util.CustomMapper;
import ru.practicum.util.EventMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRepository participationRepository;
    private final CustomMapper mapper;

    @Override
    public List<EventShortDto> getEvents(long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        QEvent qEvent = QEvent.event;
        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));
        BooleanExpression predicate = qEvent.isNotNull().and(qEvent.initiator.in(userId));

        return eventRepository.findAll(predicate, pageRequest).stream().map(l -> EventMapper.toShortFromEvent(l,
                userRepository.findByIdShort(l.getInitiator()),
                categoryRepository.findById((l.getCategory())), 0, 0)).collect(Collectors.toList());
    }

    @Override
    public EventFullDto postEvent(long userId, NewEventDto eventDto) {

        User initiator;
        Optional<User> oUser = userRepository.findById(userId);
        if (oUser.isPresent()) {
            initiator = oUser.get();
        } else {
            throw new NotFoundException();
        }
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Event date should be at least 2 hours from now");
        }

        Event event = eventRepository.save(
                Event.builder()
                        .eventDate(eventDto.getEventDate())
                        .createdOn(LocalDateTime.now())
                        .state(EventState.PENDING)
                        .initiator(userId)
                        .lat(eventDto.getLocation().getLat())
                        .lon(eventDto.getLocation().getLon())
                        .paid(eventDto.getPaid())
                        .annotation(eventDto.getAnnotation())
                        .description(eventDto.getDescription())
                        .requestModeration(eventDto.isRequestModeration())
                        .participantLimit(eventDto.getParticipantLimit())
                        .category(eventDto.getCategory())
                        .title(eventDto.getTitle())
                        .build()
        );

        return EventMapper.toFullFromEvent(event, new UserShortDto(initiator.getId(), initiator.getName()),
                categoryRepository.findById((event.getCategory())));
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        User initiator;
        Event event;
        Optional<Event> oEvent = eventRepository.findById(eventId);
        Optional<User> oUser = userRepository.findById(userId);
        if (oUser.isPresent() && oEvent.isPresent()) {
            initiator = oUser.get();
            event = oEvent.get();
        } else {
            throw new NotFoundException();
        }

        return EventMapper.toFullFromEvent(event, new UserShortDto(initiator.getId(), initiator.getName()),
                categoryRepository.findById((event.getCategory())));
    }

    @Override
    public EventFullDto patchEvent(UpdateEventUserRequest eventDto, long userId, long eventId) {
        User initiator;
        Event event;
        Optional<User> oUser = userRepository.findById(userId);
        Optional<Event> oEvent = eventRepository.findById(eventId);
        if (oUser.isPresent() && oEvent.isPresent()) {
            initiator = oUser.get();
            event = oEvent.get();
        } else {
            throw new NotFoundException();
        }
        if (!(event.getState() == EventState.PENDING || event.getState() == EventState.CANCELED)) {
            throw new ConflictException("Event state should be PENDING or CANCELED");
        }
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Event date should be at least 2 hours from now");
        }
        eventDto.setCategory(event.getCategory());

        mapper.updateEventFromDto(eventDto, event);

        if (eventDto.getStateAction() == UserStateAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        if (eventDto.getStateAction() == UserStateAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        }

        return EventMapper.toFullFromEvent(eventRepository.save(event),
                new UserShortDto(initiator.getId(), initiator.getName()),
                categoryRepository.findById((event.getCategory())));
    }

    @Override
    public EventRequestStatusUpdateResult patchEventReq(EventRequestStatusUpdateRequest request, long userId,
                                                        long eventId) {
        if (!userRepository.existsById(userId) || !eventRepository.existsById(eventId)) {
            throw new NotFoundException();
        }
        Event event = eventRepository.findById(eventId).get();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(EventRequestStatus.CONFIRMED);
        }

        Integer count = participationRepository.countAllByEventAndStatus(eventId, ParticipationStatus.CONFIRMED);
        if (event.getParticipantLimit() <= count) {

            throw new ConflictException("Event participation limit exceeded");
        }
        List<Participation> participations = participationRepository.findAllByIdIn(request.getRequestIds());

        List<Participation> confirmed = new ArrayList<>();
        List<Participation> rejected = new ArrayList<>();

        for (Participation participation : participations) {
            if (request.getStatus() == EventRequestStatus.CONFIRMED) {
                if (event.getParticipantLimit() <= count) {
                    participation.setStatus(ParticipationStatus.REJECTED);
                    rejected.add(participation);
                } else {
                    participation.setStatus(ParticipationStatus.CONFIRMED);
                    confirmed.add(participation);
                    count++;
                }
            } else {
                if (participation.getStatus() == ParticipationStatus.CONFIRMED) {
                    throw new ConflictException("Cannot reject confirmed request id:" + participation.getId());
                }
                participation.setStatus(ParticipationStatus.REJECTED);
                rejected.add(participation);
            }
        }
        participationRepository.saveAll(rejected);
        participationRepository.saveAll(confirmed);

        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }

    @Override
    public EventFullDto patchEventAdmin(UpdateEventAdminRequest eventDto, long eventId) {
        User initiator;
        Event event;
        Optional<Event> oEvent = eventRepository.findById(eventId);
        if (oEvent.isPresent()) {
            event = oEvent.get();
        } else {
            throw new NotFoundException();
        }
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ConflictException("Event date should be in future");
        }

        initiator = userRepository.findById(event.getInitiator()).get();
        eventDto.setCategory(event.getCategory());

        mapper.updateEventFromAdminDto(eventDto, event);

        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Event already published or cancelled");
        }
        if (eventDto.getStateAction() == AdminStateAction.PUBLISH_EVENT) {

            event.setState(EventState.PUBLISHED);
        }
        if (eventDto.getStateAction() == AdminStateAction.REJECT_EVENT) {

            event.setState(EventState.CANCELED);
        }
        return EventMapper.toFullFromEvent(eventRepository.save(event),
                new UserShortDto(initiator.getId(), initiator.getName()),
                categoryRepository.findById((event.getCategory())));
    }

    @Override
    public List<EventFullDto> getEventsAdmin(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd,
                                             int from,
                                             int size) {
        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        QEvent qEvent = QEvent.event;
        BooleanExpression predicate = qEvent.isNotNull();
        if (!(users == null || users.get(0) == 0)) {
            predicate = predicate.and(qEvent.initiator.in(users));
        }
        if (states != null) {
            predicate = predicate.and(qEvent.state.in(states));
        }
        if (!(categories == null || categories.get(0) == 0)) {
            predicate = predicate.and(qEvent.category.in(categories));
        }
        if (rangeStart != null) {
            predicate = predicate.and(qEvent.eventDate.after(LocalDateTime.parse(rangeStart, formatter)));
        }
        if (rangeEnd != null) {
            predicate = predicate.and(qEvent.eventDate.before(LocalDateTime.parse(rangeEnd, formatter)));
        }

        return eventRepository.findAll(predicate, pageRequest).stream().map(l -> EventMapper.toFullFromEvent(l,
                userRepository.findByIdShort(l.getInitiator()),
                categoryRepository.findById((l.getCategory())))).collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               String rangeEnd,
                                               String rangeStart,
                                               Boolean onlyAvailable,
                                               FilterEnum sort,
                                               int from, int size) {

        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        QEvent qEvent = QEvent.event;
        BooleanExpression predicate = qEvent.isNotNull();
        Comparator<EventShortDto> comparator;
        if (sort == FilterEnum.EVENT_DATE) {
            comparator = Comparator.comparing(EventShortDto::getEventDate);
        } else {
            comparator = Comparator.comparingLong(EventShortDto::getViews);
        }

        if (!(text == null || text.equals(""))) {
            predicate = predicate.and(qEvent.annotation.contains(text));
        }
        if (paid != null) {
            predicate = predicate.and(qEvent.paid.in(paid));
        }

        if (!(categories == null || categories.get(0) == 0)) {
            predicate = predicate.and(qEvent.category.in(categories));
        }
        if (rangeStart != null) {
            predicate = predicate.and(qEvent.eventDate.after(LocalDateTime.parse(rangeStart, formatter)));
        }
        if (rangeEnd != null) {
            predicate = predicate.and(qEvent.eventDate.before(LocalDateTime.parse(rangeEnd, formatter)));
        }
        if (onlyAvailable != null && onlyAvailable) {

            return eventRepository.findAll(predicate, pageRequest).stream()
                    .filter(l -> l.getParticipantLimit() > participationRepository.countAllByEventAndStatus(l.getId(),
                            ParticipationStatus.CONFIRMED))
                    .map(l -> EventMapper.toShortFromEvent(l,
                            userRepository.findByIdShort(l.getInitiator()),
                            categoryRepository.findById((l.getCategory())),
                            0,
                            participationRepository.countAllByEventAndStatus(l.getId(),
                                    ParticipationStatus.CONFIRMED)))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAll(predicate, pageRequest).stream()
                    .map(l -> EventMapper.toShortFromEvent(l,
                            userRepository.findByIdShort(l.getInitiator()),
                            categoryRepository.findById((l.getCategory())),
                            0,
                            participationRepository.countAllByEventAndStatus(l.getId(),
                                    ParticipationStatus.CONFIRMED)))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto getEventPuplic(long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException();
        }
        Event event = eventRepository.findEventByIdAndState(id, EventState.PUBLISHED);
        return EventMapper.toFullFromEvent(event, userRepository.findByIdShort(event.getInitiator()),
                categoryRepository.findById(event.getCategory()));
    }
}
