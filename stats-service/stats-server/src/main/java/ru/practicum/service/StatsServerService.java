package ru.practicum.service;

import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.model.Hit;

import java.util.List;

public interface StatsServerService {
    Hit hit(HitRequestDto dto);

    List<HitResponseDto> stats(String start, String end, List<String> uris, boolean unique);
}
