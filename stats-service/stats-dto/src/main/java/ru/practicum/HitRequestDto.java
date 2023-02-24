package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class HitRequestDto {
    @JsonFormat
    private String app;
    @JsonFormat
    private String uri;
    @JsonFormat
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;
}
