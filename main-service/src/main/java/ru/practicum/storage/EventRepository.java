package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;
import ru.practicum.model.enums.EventState;

import java.util.Collection;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Set<Event> findAllByIdIn(Collection<Long> id);

    Boolean existsByCategory(Long category);

    Event findEventByIdAndState(Long id, EventState state);
}
