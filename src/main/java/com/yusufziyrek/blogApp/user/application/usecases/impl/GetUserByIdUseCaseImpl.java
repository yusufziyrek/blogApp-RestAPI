package com.yusufziyrek.blogApp.user.application.usecases.impl;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByIdUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.shared.exception.UserException;

/**
 * Implementation of GetUserByIdUseCase
 * Clean Architecture - Application Layer Implementation
 */
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {
    
    private final UserRepository userRepository;
    
    public GetUserByIdUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserResponse execute(Long userId) {
        UserDomain user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException("UserDomain not found with id: " + userId));
        
        return mapToResponse(user);
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
