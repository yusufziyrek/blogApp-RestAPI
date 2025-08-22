package com.yusufziyrek.blogApp.user.application.usecases.impl;

import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetAllUsersUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of GetAllUsersUseCase
 * Clean Architecture - Application Layer Implementation
 */
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {
    
    private final UserRepository userRepository;
    
    public GetAllUsersUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public PageResponse<UserResponse> execute(int page, int size) {
        List<UserDomain> users = userRepository.findAll(page, size);
        long totalElements = userRepository.getTotalCount();
        
        List<UserResponse> userResponses = users.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return new PageResponse<>(
            userResponses,
            page,
            size,
            totalElements,
            (int) Math.ceil((double) totalElements / size)
        );
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
