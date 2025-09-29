package com.tasktracker.tasktrackertaskservice.controller;

import com.tasktracker.tasktrackertaskservice.dto.TaskCreateDto;
import com.tasktracker.tasktrackertaskservice.dto.TaskResponseDto;
import com.tasktracker.tasktrackertaskservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<TaskResponseDto> create(
            @Valid @RequestBody TaskCreateDto taskCreateDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        TaskResponseDto taskResponseDto = taskService.createTask(taskCreateDto, jwt);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDto);
    }
}
