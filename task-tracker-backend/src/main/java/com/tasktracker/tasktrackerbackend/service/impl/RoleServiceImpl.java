package com.tasktracker.tasktrackerbackend.service.impl;

import com.tasktracker.tasktrackerbackend.model.Role;
import com.tasktracker.tasktrackerbackend.repository.RoleRepository;
import com.tasktracker.tasktrackerbackend.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public Role getDefaultRole() {
        Optional<Role> role = roleRepository.findByName("ROLE_USER");

        return role.orElseGet(this::createDefaultRole);
    }

    private Role createDefaultRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
