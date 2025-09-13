package com.tasktracker.tasktrackeruserservice.dto;


import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto (
        String message,
        String error,
        int status,
        String path,
        LocalDateTime localDateTime
) {
    public ErrorResponseDto(String message, HttpStatus httpStatus, String path) {
        this(
                message,
                httpStatus.getReasonPhrase(),
                httpStatus.value(),
                path,
                LocalDateTime.now()
        );
    }
}
