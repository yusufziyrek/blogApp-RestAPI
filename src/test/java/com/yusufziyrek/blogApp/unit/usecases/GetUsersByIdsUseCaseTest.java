package com.yusufziyrek.blogApp.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetUsersByIdsUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.impl.GetUsersByIdsUseCaseImpl;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Unit tests for GetUsersByIdsUseCase
 * Tests batch user fetching for N+1 problem solution
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetUsersByIdsUseCase Tests")
class GetUsersByIdsUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private GetUsersByIdsUseCase getUsersByIdsUseCase;

    @BeforeEach
    void setUp() {
        getUsersByIdsUseCase = new GetUsersByIdsUseCaseImpl(userRepository);
    }

    @Nested
    @DisplayName("When fetching multiple users")
    class WhenFetchingMultipleUsers {

        @Test
        @DisplayName("Should return map with all found users")
        void shouldReturnMapWithAllFoundUsers() {
            // Given
            Set<Long> userIds = Set.of(1L, 2L, 3L);
            List<UserDomain> users = List.of(
                    createUserDomain(1L, "user1@example.com"),
                    createUserDomain(2L, "user2@example.com"),
                    createUserDomain(3L, "user3@example.com"));
            when(userRepository.findByIds(userIds)).thenReturn(users);

            // When
            Map<Long, UserResponse> result = getUsersByIdsUseCase.execute(userIds);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get(1L).getEmail()).isEqualTo("user1@example.com");
            assertThat(result.get(2L).getEmail()).isEqualTo("user2@example.com");
            assertThat(result.get(3L).getEmail()).isEqualTo("user3@example.com");
        }

        @Test
        @DisplayName("Should return empty map for empty input set")
        void shouldReturnEmptyMapForEmptyInput() {
            // Given
            Set<Long> emptySet = Set.of();

            // When
            Map<Long, UserResponse> result = getUsersByIdsUseCase.execute(emptySet);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle partial matches gracefully")
        void shouldHandlePartialMatches() {
            // Given - requesting 3 users but only 2 exist
            Set<Long> userIds = Set.of(1L, 2L, 999L);
            List<UserDomain> users = List.of(
                    createUserDomain(1L, "user1@example.com"),
                    createUserDomain(2L, "user2@example.com"));
            when(userRepository.findByIds(userIds)).thenReturn(users);

            // When
            Map<Long, UserResponse> result = getUsersByIdsUseCase.execute(userIds);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.containsKey(999L)).isFalse();
        }

        @Test
        @DisplayName("Should return single user for single ID set")
        void shouldReturnSingleUserForSingleIdSet() {
            // Given
            Set<Long> singleId = Set.of(1L);
            List<UserDomain> users = List.of(createUserDomain(1L, "user@example.com"));
            when(userRepository.findByIds(singleId)).thenReturn(users);

            // When
            Map<Long, UserResponse> result = getUsersByIdsUseCase.execute(singleId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(1L)).isNotNull();
        }
    }

    // Helper method
    private UserDomain createUserDomain(Long id, String email) {
        return new UserDomain(
                id,
                "Test",
                "User",
                "testuser" + id,
                email,
                "hashedPassword",
                "IT",
                25,
                Role.USER,
                true,
                LocalDateTime.now(),
                null);
    }
}
