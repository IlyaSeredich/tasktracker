package com.tasktracker.tasktrackerbackend.service.impl;

import com.tasktracker.tasktrackerbackend.dto.UserResponseDto;
import com.tasktracker.tasktrackerbackend.dto.UserCreateDto;
import com.tasktracker.tasktrackerbackend.exception.EmailAlreadyExistException;
import com.tasktracker.tasktrackerbackend.exception.UserAlreadyExistException;
import com.tasktracker.tasktrackerbackend.model.Role;
import com.tasktracker.tasktrackerbackend.model.User;
import com.tasktracker.tasktrackerbackend.repository.UserRepository;
import com.tasktracker.tasktrackerbackend.service.UserService;
import com.tasktracker.tasktrackerbackend.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;


    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        String username = userCreateDto.username();
        String password = userCreateDto.password();
        String email = userCreateDto.email();
        validateRegistrationConditions(username, email);
        User user = createNewUser(username, password, email);
        String jwtToken = jwtTokenUtils.generateToken(user);
        return new UserResponseDto(username, jwtToken);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
    }

    private User createNewUser(String username, String password, String email) {
        List<Role> roles = List.of(roleService.getDefaultRole());

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    private void validateRegistrationConditions(String username, String email) {
        validateUsernameNotExists(username);
        validateEmailNotExists(email);
    }

    private void validateUsernameNotExists(String username) {
        if (userExists(username)) {
            throw new UserAlreadyExistException(username);
        }
    }

    private void validateEmailNotExists(String email) {
        if (emailExists(email)) {
            throw new EmailAlreadyExistException(email);
        }
    }

    private boolean userExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

    private boolean emailExists(String email) {
        return userRepository.existsUserByEmail(email);
    }
}
