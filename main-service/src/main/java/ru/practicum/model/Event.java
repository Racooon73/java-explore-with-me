package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.enums.EventState;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonFormat
    private long id;
    @Column(length = 2048)
    @JsonFormat
    private String annotation;
    @JsonFormat
    private Long category;
    @Column(length = 2048)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @JsonFormat
    private EventState state;
    @JsonFormat
    private long initiator;
    @JsonFormat
    private float lat;
    @JsonFormat
    private float lon;
    @JsonFormat
    private Boolean paid;
    @JsonFormat
    private Integer participantLimit;
    @JsonFormat
    private Boolean requestModeration;
    @Column(length = 256)
    @JsonFormat
    private String title;
}
