package ru.practicum;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class HitRequestDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
