package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;

}
