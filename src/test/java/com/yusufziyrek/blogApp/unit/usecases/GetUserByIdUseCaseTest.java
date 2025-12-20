package com.yusufziyrek.blogApp.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByIdUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.impl.GetUserByIdUseCaseImpl;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Unit tests for GetUserByIdUseCase
 * Tests user retrieval by ID with mocked repository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetUserByIdUseCase Tests")
class GetUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private GetUserByIdUseCase getUserByIdUseCase;

    @BeforeEach
    void setUp() {
        getUserByIdUseCase = new GetUserByIdUseCaseImpl(userRepository);
    }

    @Nested
    @DisplayName("When user exists")
    class WhenUserExists {

        @Test
        @DisplayName("Should return user response with all fields")
        void shouldReturnUserResponseWithAllFields() {
            // Given
            Long userId = 1L;
            UserDomain userDomain = createUserDomain(userId, "test@example.com");
            when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));

            // When
            UserResponse result = getUserByIdUseCase.execute(userId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(userId);
            assertThat(result.getEmail()).isEqualTo("test@example.com");
            assertThat(result.getUsername()).isEqualTo("testuser");
            assertThat(result.getFirstname()).isEqualTo("Test");
            assertThat(result.getLastname()).isEqualTo("User");
            assertThat(result.getRole()).isEqualTo(Role.USER);
        }

        @Test
        @DisplayName("Should return correct role for admin user")
        void shouldReturnCorrectRoleForAdminUser() {
            // Given
            Long userId = 2L;
            UserDomain adminUser = new UserDomain(
                    userId,
                    "Admin",
                    "User",
                    "adminuser",
                    "admin@example.com",
                    "hashedPassword",
                    "IT",
                    30,
                    Role.ADMIN,
                    true,
                    LocalDateTime.now(),
                    null);
            when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));

            // When
            UserResponse result = getUserByIdUseCase.execute(userId);

            // Then
            assertThat(result.getRole()).isEqualTo(Role.ADMIN);
        }
    }

    @Nested
    @DisplayName("When user does not exist")
    class WhenUserDoesNotExist {

        @Test
        @DisplayName("Should throw UserException with NOT_FOUND status")
        void shouldThrowUserExceptionWithNotFoundStatus() {
            // Given
            Long nonExistentId = 999L;
            when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> getUserByIdUseCase.execute(nonExistentId))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("999")
                    .satisfies(thrown -> {
                        UserException exception = (UserException) thrown;
                        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    });
        }

        @Test
        @DisplayName("Should throw exception for null ID")
        void shouldThrowExceptionForNullId() {
            // Given
            when(userRepository.findById(null)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> getUserByIdUseCase.execute(null))
                    .isInstanceOf(UserException.class);
        }
    }

    // Helper method
    private UserDomain createUserDomain(Long id, String email) {
        return new UserDomain(
                id,
                "Test", // firstname
                "User", // lastname
                "testuser", // username
                email,
                "hashedPassword",
                "IT", // department
                25, // age
                Role.USER,
                true, // enabled
                LocalDateTime.now(),
                null // updatedDate
        );
    }
}
