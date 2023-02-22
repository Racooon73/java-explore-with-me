package ru.practicum.model.requests;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.enums.EventRequestStatus;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private EventRequestStatus status;
}
