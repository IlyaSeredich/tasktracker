package com.tasktracker.tasktrackerbackend.dto;

import com.tasktracker.tasktrackerbackend.validation.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotBlank(message = "Username must not be blank")
        @Size(min = 4, max = 15, message = "Username must be between 4 and 15 characters long")
        String username,
        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, message = "Password must have at least 6 characters")
        String password,
        @NotBlank(message = "Email must not be blank")
        @Email
        @ValidEmail
        String email
){}
