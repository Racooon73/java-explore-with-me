package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Hit;
import ru.practicum.service.StatsServerService;

import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class StatsController {
    StatsServerService serverService;

    @PostMapping("/hit")
    public Hit hit(@RequestBody HitRequestDto dto) {
        log.info("Получен запрос POST /hit");
        return serverService.hit(dto);
    }

    @GetMapping("/stats")
    public List<HitResponseDto> stats(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(required = false) boolean unique) {
        log.info("Получен запрос GET /stats");
        return serverService.stats(start, end, uris, unique);
    }
}
