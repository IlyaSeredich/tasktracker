package com.tasktracker.tasktrackertaskservice.dto;

public record TaskResponseDto(
        String title,
        String description,
        String status
) {
}
