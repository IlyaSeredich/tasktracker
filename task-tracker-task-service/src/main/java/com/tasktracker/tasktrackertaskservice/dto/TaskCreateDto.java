package com.tasktracker.tasktrackertaskservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateDto(
        @NotBlank(message = "Title must not be blank")
        @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters long")
        String title,
        String description
) {

}
