package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<ApiError> sqlConflictException(final DataIntegrityViolationException e) {
        log.error("conflictException ");


        return ResponseEntity.status(409).body(ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .reason("Not unique name")
                .status(HttpStatus.CONFLICT.toString()).build());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> conflictException(final ConflictException e) {
        log.error("conflictException ");

        return ResponseEntity.status(409).body(ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT.toString()).build());
    }
}
