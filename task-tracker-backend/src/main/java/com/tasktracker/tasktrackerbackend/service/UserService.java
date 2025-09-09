package com.tasktracker.tasktrackerbackend.service;

import com.tasktracker.tasktrackerbackend.dto.UserCreateDto;
import com.tasktracker.tasktrackerbackend.dto.UserResponseDto;
import com.tasktracker.tasktrackerbackend.model.User;

public interface UserService {
    UserResponseDto createUser(UserCreateDto userCreateDto);
    User getUserByUsername(String username);
}
