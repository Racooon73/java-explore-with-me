package ru.practicum.service;

import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Participation;

import java.util.List;

public interface ParticipationService {
    List<Participation> getEventReq(long userId, long eventId) throws BadRequestException;

    List<Participation> getEventReqByUser(long userId);

    Participation postEventReqByUser(long userId, long eventId);

    Participation cancelEventReqByUser(long userId, long requestId);
}
