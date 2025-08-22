package com.yusufziyrek.blogApp.auth.infrastructure.usecases.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.auth.application.ports.usecases.RegisterUserUseCase;
import com.yusufziyrek.blogApp.auth.dto.request.RegisterRequest;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.user.application.ports.PasswordEncoder;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.user.infrastructure.mappers.UserMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of RegisterUserUseCase
 * Clean Architecture - Infrastructure Layer (Use Case Implementation)
 */
@Service
@Slf4j
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCaseImpl(UserRepository userRepository, 
                                  @Qualifier("userPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse execute(RegisterRequest registerRequest) {
        log.info("Starting user registration for email: {}", registerRequest.getEmail());
        
        // Validate passwords match
        validatePasswordsMatch(registerRequest);
        
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Registration failed: Username already exists: {}", registerRequest.getUsername());
            throw new UserException("Username '" + registerRequest.getUsername() + "' is already taken");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed: Email already exists: {}", registerRequest.getEmail());
            throw new UserException("Email '" + registerRequest.getEmail() + "' is already registered");
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
        
        log.info("User registered successfully with ID: {} and email: {}", 
                savedUser.getId(), savedUser.getEmail());
        
        // Convert to response DTO
        return UserMapper.toResponse(savedUser);
    }
    
    private void validatePasswordsMatch(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new UserException("Password and confirm password do not match");
        }
    }
}
