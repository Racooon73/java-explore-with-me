package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Comment;
import ru.practicum.model.Participation;
import ru.practicum.model.dto.*;
import ru.practicum.model.requests.EventRequestStatusUpdateRequest;
import ru.practicum.model.requests.EventRequestStatusUpdateResult;
import ru.practicum.model.requests.UpdateEventUserRequest;
import ru.practicum.service.CommentService;
import ru.practicum.service.EventService;
import ru.practicum.service.ParticipationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateController {

    private final EventService eventService;
    private final ParticipationService participationService;
    private final CommentService commentService;

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("GET /users/" + userId + "/events");
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable long userId,
                                  @RequestBody @Valid NewEventDto eventDto) {
        log.info("POST /users/" + userId + "/events");
        return eventService.postEvent(userId, eventDto);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEvent(@PathVariable long userId,
                                 @PathVariable long eventId,
                                 HttpServletRequest request) {
        log.info("GET /users/" + userId + "/events/" + eventId);
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(@RequestBody UpdateEventUserRequest event,
                                   @PathVariable long userId,
                                   @PathVariable long eventId) {
        log.info("PATCH /users/" + userId + "/events/" + eventId);
        return eventService.patchEvent(event, userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<Participation> getEventReq(@PathVariable long userId,
                                           @PathVariable long eventId) throws BadRequestException {
        log.info("GET /users/" + userId + "/events/" + eventId + "/requests");
        return participationService.getEventReq(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventReq(@RequestBody EventRequestStatusUpdateRequest request,
                                                        @PathVariable long userId,
                                                        @PathVariable long eventId) {
        log.info("PATCH /users/" + userId + "/events/" + eventId + "/requests");
        return eventService.patchEventReq(request, userId, eventId);
    }

    @GetMapping("/requests")
    public List<Participation> getEventReqByUser(@PathVariable long userId) {
        log.info("GET /users/" + userId + "/requests");
        return participationService.getEventReqByUser(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public Participation postEventReqByUser(@PathVariable long userId,
                                            @RequestParam long eventId) {
        log.info("POST /users/" + userId + "/requests");
        return participationService.postEventReqByUser(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public Participation cancelEventReqByUser(@PathVariable long userId,
                                              @PathVariable long requestId) {
        log.info("GET /users/" + userId + "/requests/" + requestId + "/cancel");
        return participationService.cancelEventReqByUser(userId, requestId);
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment postComment(@PathVariable long userId,
                               @RequestParam long eventId,
                               @RequestBody @Valid CommentDto dto) {
        log.info("POST /users/" + userId + "/comments");
        return commentService.postComment(userId, eventId, dto);
    }

    @PatchMapping("/comments/{comId}")
    public Comment patchComment(@PathVariable long userId,
                                @PathVariable long comId,
                                @RequestBody @Valid PatchCommentDto dto) {
        log.info("PATCH /users/" + userId + "/comments/" + comId);
        return commentService.patchComment(userId, comId, dto);
    }

    @GetMapping("/comments")
    public List<Comment> getCommentsByCurrentUser(@PathVariable long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("PATCH /users/" + userId + "/comments");
        return commentService.getCommentsByCurrentUser(userId, from, size);
    }

    @DeleteMapping("/comments/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId,
                              @PathVariable long comId) {
        log.info("DELETE /users/" + userId + "/comments/" + comId);
        commentService.deleteComment(userId, comId);
    }

}
