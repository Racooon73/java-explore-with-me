package ru.practicum.service;

import ru.practicum.model.Comment;
import ru.practicum.model.dto.CommentDto;
import ru.practicum.model.dto.PatchCommentDto;

import java.util.List;

public interface CommentService {
    Comment postComment(long userId, long eventId, CommentDto dto);

    Comment patchComment(long userId, long comId, PatchCommentDto dto);

    List<Comment> getCommentsByCurrentUser(long userId, int from, int size);

    void deleteComment(long userId, long comId);

    Comment patchCommentAdmin(long comId, PatchCommentDto dto);

    void deleteCommentAdmin(long comId);

    List<Comment> getCommentsByEvent(long eventId, int from, int size);

    Comment getCommentById(long comId);
}
