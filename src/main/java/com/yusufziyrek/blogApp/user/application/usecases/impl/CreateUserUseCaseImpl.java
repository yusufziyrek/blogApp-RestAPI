package com.yusufziyrek.blogApp.user.application.usecases.impl;

import com.yusufziyrek.blogApp.user.application.ports.PasswordEncoder;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.CreateUserUseCase;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.request.CreateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.shared.exception.UserException;

/**
 * Create user use case implementasyonu
 */
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public CreateUserUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserResponse execute(CreateUserRequest request) {
    // Domain doğrulamaları
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserException("Username already exists: " + request.getUsername());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email already exists: " + request.getEmail());
        }
        
    // Domain nesnesini oluştur
        UserDomain user = new UserDomain(
            request.getFirstname(),
            request.getLastname(),
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getDepartment(),
            request.getAge(),
            Role.USER // Default role
        );
        
    // İş kuralları doğrulaması
        if (!user.hasValidEmail()) {
            throw new UserException("Invalid email format");
        }
        
        if (!user.hasValidUsername()) {
            throw new UserException("Invalid username format");
        }
        
        if (!user.hasValidAge()) {
            throw new UserException("Invalid age");
        }
        
    // Kaydet
        UserDomain savedUser = userRepository.save(user);
        
    // Yanıt döndür
        return mapToResponse(savedUser);
    }
    
    private UserResponse mapToResponse(UserDomain user) {
        return new UserResponse(
            user.getId(),
            user.getFirstname(),
            user.getLastname(),
            user.getUsername(),
            user.getEmail(),
            user.getDepartment(),
            user.getAge(),
            user.getRole(),
            user.isEnabled(),
            user.getCreatedDate(),
            user.getUpdatedDate()
        );
    }
}
