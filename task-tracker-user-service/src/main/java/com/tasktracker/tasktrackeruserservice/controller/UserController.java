package com.tasktracker.tasktrackeruserservice.controller;

import com.tasktracker.tasktrackeruserservice.dto.UserCreateDto;
import com.tasktracker.tasktrackeruserservice.keycloak.KeycloakUtils;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final KeycloakUtils keycloakUtils;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        Response response = keycloakUtils.createKeycloakUser(userCreateDto);
        if(response.getStatus() == HttpStatus.CONFLICT.value()) {
            return ResponseEntity.status(response.getStatus())
                    .body("user already exists");

        }
        String userId = CreatedResponseUtil.getCreatedId(response);
        System.out.println("Created users id is - " + userId);

        List<String> defaultRoles = List.of("user");

        keycloakUtils.addRoles(userId, defaultRoles);
        return ResponseEntity.status(response.getStatus()).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        keycloakUtils.logoutCurrentSession(jwt);

        return ResponseEntity.ok().build();
    }
}
