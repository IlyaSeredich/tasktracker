package com.tasktracker.tasktrackertaskservice.service;

import com.tasktracker.tasktrackerentity.model.Status;
import com.tasktracker.tasktrackerentity.model.Task;
import com.tasktracker.tasktrackertaskservice.dto.TaskCreateDto;
import com.tasktracker.tasktrackertaskservice.dto.TaskResponseDto;
import com.tasktracker.tasktrackertaskservice.repository.StatusRepository;
import com.tasktracker.tasktrackertaskservice.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;

    public TaskResponseDto createTask(TaskCreateDto taskCreateDto, Jwt jwt) {
        Status status = statusRepository.findByName("NEW").orElseThrow(() -> new RuntimeException("xxxx"));
        Task task = new Task();
        task.setTitle(taskCreateDto.title());
        task.setDescription(taskCreateDto.description());
        task.setUserId(jwt.getSubject());
        task.setStatus(status);
        task.setCreatedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);

        return new TaskResponseDto(saved.getTitle(), saved.getDescription(), task.getStatus().getName());
    }
}
