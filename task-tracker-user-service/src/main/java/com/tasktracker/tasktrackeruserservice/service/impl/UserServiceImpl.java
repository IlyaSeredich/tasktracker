package com.tasktracker.tasktrackeruserservice.service.impl;


import com.tasktracker.tasktrackerentity.model.Role;
import com.tasktracker.tasktrackerentity.model.User;
import com.tasktracker.tasktrackeruserservice.dto.UserAuthDto;
import com.tasktracker.tasktrackeruserservice.dto.UserCreateDto;
import com.tasktracker.tasktrackeruserservice.dto.UserResponseDto;
import com.tasktracker.tasktrackeruserservice.exception.EmailAlreadyExistException;
import com.tasktracker.tasktrackeruserservice.exception.UserAlreadyExistException;
import com.tasktracker.tasktrackeruserservice.repository.UserRepository;
import com.tasktracker.tasktrackeruserservice.service.AuthService;
import com.tasktracker.tasktrackeruserservice.service.UserService;
import com.tasktracker.tasktrackeruserservice.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final AuthService authService;
    private final UserDetailsService userDetailsService;


    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        String username = userCreateDto.username();
        String password = userCreateDto.password();
        String email = userCreateDto.email();
        validateRegistrationConditions(username, email);
        User user = createNewUser(username, password, email);
        String jwtToken = jwtTokenUtils.generateToken(user);
        return new UserResponseDto(username, jwtToken);
    }

    @Override
    public UserResponseDto authorizeUser(UserAuthDto userAuthDto) {
        String username = userAuthDto.getUsername();
        String password = userAuthDto.getPassword();
        authService.authenticateUser(username, password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwtToken = jwtTokenUtils.generateToken(userDetails);
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
