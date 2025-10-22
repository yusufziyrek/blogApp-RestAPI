package com.yusufziyrek.blogApp.auth.application.usecases.impl;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.auth.application.usecases.RegisterUserUseCase;
import com.yusufziyrek.blogApp.auth.dto.request.RegisterRequest;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.user.application.ports.PasswordEncoder;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.user.infrastructure.mappers.UserMapper;

/**
 * Implementation of RegisterUserUseCase
 * Clean Architecture - Application Layer (Use Case Implementation)
 */
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse execute(RegisterRequest registerRequest) {
        
        // Validate passwords match
        validatePasswordsMatch(registerRequest);
        
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserException(String.format(ErrorMessages.USERNAME_ALREADY_EXISTS, registerRequest.getUsername()), HttpStatus.CONFLICT);
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserException(String.format(ErrorMessages.EMAIL_ALREADY_EXISTS, registerRequest.getEmail()), HttpStatus.CONFLICT);
        }
        
        // Encode password
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        
        // Create new user domain object
        UserDomain newUser = new UserDomain(
            registerRequest.getFirstname(),
            registerRequest.getLastname(),
            registerRequest.getUsername(),
            registerRequest.getEmail(),
            encodedPassword,
            registerRequest.getDepartment(),
            registerRequest.getAge(),
            Role.USER // Default role for new registrations
        );
        
        // Save user
        UserDomain savedUser = userRepository.save(newUser);
        
        // Convert to response DTO
        return UserMapper.toResponse(savedUser);
    }
    
    private void validatePasswordsMatch(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new UserException(ErrorMessages.PASSWORD_MISMATCH);
        }
    }
}
