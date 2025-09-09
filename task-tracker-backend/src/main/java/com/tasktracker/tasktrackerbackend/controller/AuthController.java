package com.tasktracker.tasktrackerbackend.controller;

import com.tasktracker.tasktrackerbackend.dto.UserCreateDto;
import com.tasktracker.tasktrackerbackend.dto.UserResponseDto;
import com.tasktracker.tasktrackerbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserCreateDto userCreateDto) {
        UserResponseDto userResponseDto = userService.createUser(userCreateDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userResponseDto.jwtToken())
                .body(userResponseDto);

    }
}
