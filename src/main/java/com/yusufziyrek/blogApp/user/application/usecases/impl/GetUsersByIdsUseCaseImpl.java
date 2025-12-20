package com.yusufziyrek.blogApp.user.application.usecases.impl;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetUsersByIdsUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of GetUsersByIdsUseCase
 * Clean Architecture - Application Layer Implementation
 * Used for batch fetching to avoid N+1 queries
 */
public class GetUsersByIdsUseCaseImpl implements GetUsersByIdsUseCase {

    private final UserRepository userRepository;

    public GetUsersByIdsUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<Long, UserResponse> execute(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        List<UserDomain> users = userRepository.findByIds(userIds);

        return users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
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
                user.getUpdatedDate());
    }
}
