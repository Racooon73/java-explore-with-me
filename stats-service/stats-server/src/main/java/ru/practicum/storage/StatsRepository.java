package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    List<Hit> findDistinctByUriInAndTimestampAfterAndTimestampBefore(List<String> uris, LocalDateTime start,
                                                                     LocalDateTime end);


    Long countDistinctByUri(String uri);

    Long countByUri(String uri);


}
