package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.model.Hit;
import ru.practicum.storage.StatsRepository;
import ru.practicum.util.HitMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsServerServiceImpl implements StatsServerService {

    final StatsRepository statsRepository;

    @Override
    public Hit hit(HitRequestDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.parse(dto.getTimestamp(), formatter);
        return statsRepository.save(Hit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(timestamp)
                .build());
    }

    @Override
    public List<HitResponseDto> stats(String start, String end, List<String> uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        List<Hit> hits = statsRepository.findDistinctByUriInAndTimestampAfterAndTimestampBefore(uris, startDate, endDate);
        if (unique) {
            return hits.stream()
                    .map(l -> HitMapper.fromHitToHitResponse(l, statsRepository.countDistinctByUri(l.getUri())))
                    .distinct()
                    .sorted(Comparator.comparingLong(HitResponseDto::getHits).reversed())
                    .collect(Collectors.toList());
        } else {
            return hits.stream()
                    .map(l -> HitMapper.fromHitToHitResponse(l, statsRepository.countByUri(l.getUri())))
                    .distinct()
                    .sorted(Comparator.comparingLong(HitResponseDto::getHits).reversed())
                    .collect(Collectors.toList());
        }
    }
}
