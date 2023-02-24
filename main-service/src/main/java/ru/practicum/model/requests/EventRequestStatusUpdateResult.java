package ru.practicum.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.Participation;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<Participation> confirmedRequests;
    private List<Participation> rejectedRequests;
}
