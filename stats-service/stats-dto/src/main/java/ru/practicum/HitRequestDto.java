package ru.practicum;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class HitRequestDto {
    String app;
    String uri;
    String ip;
    String timestamp;
}
