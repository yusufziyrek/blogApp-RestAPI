package com.yusufziyrek.blogApp.user.application.usecases;

import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import java.util.Map;
import java.util.Set;

/**
 * Use Case Interface for Getting Multiple Users by IDs
 * Clean Architecture - Application Layer
 * Used for batch fetching to avoid N+1 queries
 */
public interface GetUsersByIdsUseCase {
    /**
     * Executes the use case to fetch multiple users by their IDs
     * 
     * @param userIds Set of user IDs to fetch
     * @return Map of userId to UserResponse
     */
    Map<Long, UserResponse> execute(Set<Long> userIds);
}
