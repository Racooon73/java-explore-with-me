package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatsClient;
import ru.practicum.model.Category;
import ru.practicum.model.Comment;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.enums.FilterEnum;
import ru.practicum.service.CategoryService;
import ru.practicum.service.CommentService;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicController {

    private final StatsClient statsClient;
    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("GET /compilations");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        log.info("GET /compilations/" + compId);
        return compilationService.getCompilation(compId);
    }

    @GetMapping("/categories")
    public List<Category> getCategories(@RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("GET /categories");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public Category getCategoryById(@PathVariable long catId) {
        log.info("GET /categories" + catId);
        return categoryService.getCategory(catId);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(required = false) Boolean onlyAvailable,
                                         @RequestParam(required = false) FilterEnum sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("GET /events");
        statsClient.hit(request);
        return eventService.getEventsPublic(text, categories, paid, rangeEnd, rangeStart, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable long id,
                                     HttpServletRequest request) {
        log.info("GET /events" + id);
        statsClient.hit(request);
        return eventService.getEventPuplic(id);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<Comment> getCommentsByEvent(@PathVariable long eventId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /events/" + eventId + "/comments");

        return commentService.getCommentsByEvent(eventId, from, size);
    }

    @GetMapping("/comments/{comId}")
    public Comment getCommentById(@PathVariable long comId) {
        log.info("GET /comments/" + comId);

        return commentService.getCommentById(comId);
    }

}
