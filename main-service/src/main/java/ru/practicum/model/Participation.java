package ru.practicum.model;

import lombok.*;
import ru.practicum.model.enums.ParticipationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "participations")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime created;
    @Column(name = "event_id")
    private long event;
    @Column(name = "requester_id")
    private long requester;
    private ParticipationStatus status;
}
