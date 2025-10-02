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
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final KeycloakUtils keycloakUtils;

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String userId) {
        keycloakUtils.deleteUserById(userId);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/find")
    public ResponseEntity<?> find(@RequestBody String userId) {
        return ResponseEntity.ok(keycloakUtils.findUserById(userId));

    }
}
