package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.dto.CommentDto;
import ru.practicum.model.dto.PatchCommentDto;
import ru.practicum.model.enums.EventState;
import ru.practicum.storage.CommentRepository;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Comment postComment(long userId, long eventId, CommentDto dto) {
        if (!(userRepository.existsById(userId) || eventRepository.existsById(eventId))) {
            throw new NotFoundException();
        }
        Event event = eventRepository.findById(eventId).get();
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event must be published");
        }
        return commentRepository.save(Comment.builder()
                .posted(LocalDateTime.now())
                .title(dto.getTitle())
                .text(dto.getText())
                .authorId(userId)
                .eventId(eventId)
                .redacted(false)
                .build());
    }

    @Override
    public Comment patchComment(long userId, long comId, PatchCommentDto dto) {
        if (!(userRepository.existsById(userId) || commentRepository.existsById(comId))) {
            throw new NotFoundException();
        }
        Comment comment = commentRepository.findById(comId).get();
        if (comment.getAuthorId() != userId) {
            throw new ConflictException("User must be author of comment");
        }
        if (dto.getTitle() == null) {
            dto.setTitle(comment.getTitle());
        }
        comment.setText(dto.getText());
        comment.setTitle(dto.getTitle());
        comment.setRedacted(true);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByCurrentUser(long userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }

        return commentRepository.findAllByAuthorId(userId, pageRequest);
    }

    @Override
    public void deleteComment(long userId, long comId) {
        if (!(userRepository.existsById(userId) || commentRepository.existsById(comId))) {
            throw new NotFoundException();
        }
        Comment comment = commentRepository.findById(comId).get();
        if (comment.getAuthorId() != userId) {
            throw new ConflictException("User must be author of comment");
        }
        commentRepository.deleteById(comId);
    }

    @Override
    public Comment patchCommentAdmin(long comId, PatchCommentDto dto) {
        if (!commentRepository.existsById(comId)) {
            throw new NotFoundException();
        }
        Comment comment = commentRepository.findById(comId).get();
        if (dto.getTitle() == null) {
            dto.setTitle(comment.getTitle());
        }
        comment.setText(dto.getText());
        comment.setTitle(dto.getTitle());
        comment.setRedacted(true);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteCommentAdmin(long comId) {
        if (!commentRepository.existsById(comId)) {
            throw new NotFoundException();
        }
        commentRepository.deleteById(comId);
    }

    @Override
    public List<Comment> getCommentsByEvent(long eventId, int from, int size) {
        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException();
        }
        Event event = eventRepository.findById(eventId).get();
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event must be published");
        }

        return commentRepository.findAllByEventId(eventId, pageRequest);
    }

    @Override
    public Comment getCommentById(long comId) {
        if (!commentRepository.existsById(comId)) {
            throw new NotFoundException();
        }
        return commentRepository.findById(comId).get();
    }
}
