package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Participation;
import ru.practicum.model.enums.ParticipationStatus;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRequesterIs(Long requester);

    List<Participation> findAllByEventIs(Long eventId);

    List<Participation> findAllByIdIn(List<Long> ids);

    Integer countAllByEventAndStatus(Long event, ParticipationStatus status);

    Boolean existsByEventAndRequester(Long event, Long requester);

    Integer countByEvent(Long event);
}
