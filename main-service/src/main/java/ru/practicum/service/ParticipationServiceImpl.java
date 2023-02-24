package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.ParticipationStatus;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.ParticipationRepository;
import ru.practicum.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<Participation> getEventReq(long userId, long eventId) throws BadRequestException {
        if (!userRepository.existsById(userId) || !eventRepository.existsById(eventId)) {
            throw new NotFoundException();
        }
        Event event = eventRepository.findById(eventId).get();
        if (event.getInitiator() != userId) {
            throw new BadRequestException();
        }
        return participationRepository.findAllByEventIs(eventId);

    }

    @Override
    public List<Participation> getEventReqByUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        return participationRepository.findAllByRequesterIs(userId);
    }

    @Override
    public Participation postEventReqByUser(long userId, long eventId) {
        if (!userRepository.existsById(userId) || !eventRepository.existsById(eventId)) {
            throw new NotFoundException();
        }
        Event event = eventRepository.findById(eventId).get();
        ParticipationStatus status;


        if (event.getInitiator() == userId) {
            throw new ConflictException("Initiator of event cannot post request");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event not published");
        }
        if (event.getRequestModeration()) {
            status = ParticipationStatus.PENDING;

        } else {
            Integer count = participationRepository.countByEvent(eventId);
            if (event.getParticipantLimit() <= count) {
                throw new ConflictException("Event participation limit exceeded");
            }
            status = ParticipationStatus.CONFIRMED;
        }


        if (participationRepository.existsByEventAndRequester(eventId, userId)) {
            throw new ConflictException("Request already exist");
        }


        return participationRepository.save(Participation.builder()
                .event(eventId)
                .created(LocalDateTime.now())
                .requester(userId)
                .status(status)
                .build());
    }

    @Override
    public Participation cancelEventReqByUser(long userId, long requestId) {
        if (!userRepository.existsById(userId) || !participationRepository.existsById(requestId)) {
            throw new NotFoundException();
        }
        Participation participation = participationRepository.findById(requestId).get();
        participation.setStatus(ParticipationStatus.CANCELED);
        return participationRepository.save(participation);
    }
}
