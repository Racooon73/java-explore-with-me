package ru.practicum;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class HitResponseDto {
    private String app;
    private String uri;
    private long hits;
}
