package ru.practicum;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ViewsStatsRequest {
    private String application;
    private Integer limit;
    private boolean unique;
    @Singular("uri")
    private Set<String> uris;
    @Builder.Default
    private LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0);
    @Builder.Default
    private LocalDateTime end = LocalDateTime.now();

    public boolean hasUriCondition() {
        return uris != null && !uris.isEmpty();
    }

    public boolean hasLimitCondition() {
        return limit != null && limit != 0;
    }
}
