package ru.practicum;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class HitResponseDto {
    String app;
    String uri;
    long hits;
}
