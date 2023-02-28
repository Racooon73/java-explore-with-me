package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Category;
import ru.practicum.model.Comment;
import ru.practicum.model.User;
import ru.practicum.model.dto.*;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.requests.NewUserRequest;
import ru.practicum.model.requests.UpdateCompilationRequest;
import ru.practicum.model.requests.UpdateEventAdminRequest;
import ru.practicum.service.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final CommentService commentService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody @Valid NewCategoryDto dto) {
        log.info("POST /admin/categories");
        return categoryService.addCategory(dto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("DELETE /admin/categories/" + catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public Category patchCategory(@PathVariable long catId,
                                  @RequestBody @Valid NewCategoryDto category) {
        log.info("PATCH /admin/categories/" + catId);
        return categoryService.patchCategory(catId, category);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid @NotNull NewCompilationDto dto) {
        log.info("POST /admin/compilations");
        return compilationService.addCompilation(dto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        log.info("DELETE /admin/compilations/" + compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto patchCompilation(@PathVariable long compId,
                                           @RequestBody UpdateCompilationRequest compilation) {
        log.info("PATCH /admin/compilations/" + compId);
        return compilationService.patchCompilation(compId, compilation);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody @Valid NewUserRequest dto) {
        log.info("POST /admin/users");
        return userService.addUser(dto);
    }

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam List<Long> ids,
                               @RequestParam(defaultValue = "0") int from,
                               @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/users");
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("DELETE /admin/users/" + userId);
        userService.deleteUser(userId);

    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<EventState> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/events");
        return eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(@RequestBody UpdateEventAdminRequest event,
                                   @PathVariable long eventId) {
        log.info("GET /admin/events/" + eventId);
        return eventService.patchEventAdmin(event, eventId);
    }

    @PatchMapping("/comments/{comId}")
    public Comment patchCommentAdmin(@PathVariable long comId,
                                     @RequestBody @Valid PatchCommentDto dto) {
        log.info("PATCH /admin/comments/" + comId);
        return commentService.patchCommentAdmin(comId, dto);
    }

    @DeleteMapping("/comments/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable long comId) {
        log.info("DELETE /admin/comments/" + comId);
        commentService.deleteCommentAdmin(comId);
    }
}
