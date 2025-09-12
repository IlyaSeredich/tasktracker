package com.tasktracker.tasktrackeruserservice.service;


import com.tasktracker.tasktrackerentity.model.User;
import com.tasktracker.tasktrackeruserservice.dto.UserAuthDto;
import com.tasktracker.tasktrackeruserservice.dto.UserCreateDto;
import com.tasktracker.tasktrackeruserservice.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserCreateDto userCreateDto);
    User getUserByUsername(String username);
    UserResponseDto authorizeUser(UserAuthDto userAuthDto);
}
