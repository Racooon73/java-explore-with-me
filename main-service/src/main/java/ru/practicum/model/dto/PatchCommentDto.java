package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatchCommentDto {
    private String title;
    @NotNull
    @NotBlank
    private String text;
}
