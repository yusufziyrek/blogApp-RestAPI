package com.yusufziyrek.blogApp.user.application.usecases.impl;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByUsernameOrEmailUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.shared.exception.UserException;

/**
 * Implementation of GetUserByUsernameOrEmailUseCase
 * Clean Architecture - Application Layer Implementation
 */
public class GetUserByUsernameOrEmailUseCaseImpl implements GetUserByUsernameOrEmailUseCase {
    
    private final UserRepository userRepository;
    
    public GetUserByUsernameOrEmailUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserResponse execute(String usernameOrEmail) {
        UserDomain user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new UserException("UserDomain not found with username or email: " + usernameOrEmail));
        
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
