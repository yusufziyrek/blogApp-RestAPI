package com.yusufziyrek.blogApp.user.application.usecases.impl;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByEmailUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.shared.exception.UserException;

/**
 * Implementation of GetUserByEmailUseCase
 * Clean Architecture - Application Layer Implementation
 */
public class GetUserByEmailUseCaseImpl implements GetUserByEmailUseCase {
    
    private final UserRepository userRepository;
    
    public GetUserByEmailUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserResponse execute(String email) {
        UserDomain user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException("UserDomain not found with email: " + email));
        
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
