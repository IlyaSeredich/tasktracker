package com.tasktracker.tasktrackeruserservice.controller;

import com.tasktracker.tasktrackeruserservice.dto.UserAuthDto;
import com.tasktracker.tasktrackeruserservice.dto.UserCreateDto;
import com.tasktracker.tasktrackeruserservice.dto.UserResponseDto;
import com.tasktracker.tasktrackeruserservice.service.UserService;
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
    private static final String TOKEN_TEMPLATE = "Bearer %s";

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserCreateDto userCreateDto) {
        UserResponseDto userResponseDto = userService.createUser(userCreateDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, String.format(TOKEN_TEMPLATE, userResponseDto.jwtToken()))
                .body(userResponseDto);

    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserResponseDto> authorize(@RequestBody @Valid UserAuthDto userAuthDto) {
        UserResponseDto userResponseDto = userService.authorizeUser(userAuthDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, String.format(TOKEN_TEMPLATE, userResponseDto.jwtToken()))
                .body(userResponseDto);
    }
}
