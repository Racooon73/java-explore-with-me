package ru.practicum.util;

import lombok.NoArgsConstructor;
import ru.practicum.HitResponseDto;
import ru.practicum.model.Hit;

@NoArgsConstructor
public class HitMapper {
    public static HitResponseDto fromHitToHitResponse(Hit hit, long hits) {

        return HitResponseDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .hits(hits)
                .build();
    }
}
