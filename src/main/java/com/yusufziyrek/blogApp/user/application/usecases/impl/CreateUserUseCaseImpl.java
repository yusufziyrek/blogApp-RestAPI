package com.yusufziyrek.blogApp.user.application.usecases.impl;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.user.application.ports.PasswordEncoder;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.CreateUserUseCase;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.request.CreateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

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
            throw new UserException(String.format(ErrorMessages.USERNAME_ALREADY_EXISTS, request.getUsername()), HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException(String.format(ErrorMessages.EMAIL_ALREADY_EXISTS, request.getEmail()), HttpStatus.CONFLICT);
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
            throw new UserException(ErrorMessages.INVALID_EMAIL_FORMAT);
        }
        
        if (!user.hasValidUsername()) {
            throw new UserException(ErrorMessages.INVALID_USERNAME_FORMAT);
        }
        
        if (!user.hasValidAge()) {
            throw new UserException(ErrorMessages.AGE_RANGE_INVALID);
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
