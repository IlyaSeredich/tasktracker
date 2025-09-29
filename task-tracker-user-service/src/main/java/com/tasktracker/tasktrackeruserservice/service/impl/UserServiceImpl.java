package com.tasktracker.tasktrackeruserservice.service.impl;

import com.tasktracker.tasktrackeruserservice.dto.UserCreateDto;
import com.tasktracker.tasktrackeruserservice.keycloak.KeycloakUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl {
    private final KeycloakUtils keycloakUtils;

    public void createUser(UserCreateDto userCreateDto) {

    }
}
