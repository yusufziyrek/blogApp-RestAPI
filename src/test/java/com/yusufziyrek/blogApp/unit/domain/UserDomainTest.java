package com.yusufziyrek.blogApp.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

/**
 * Unit tests for UserDomain
 * Tests domain business logic and validation methods
 */
@DisplayName("UserDomain Tests")
class UserDomainTest {

    private UserDomain user;

    @BeforeEach
    void setUp() {
        user = new UserDomain(
                1L,
                "John",
                "Doe",
                "johndoe",
                "john@example.com",
                "hashedPassword",
                "Engineering",
                25,
                Role.USER,
                true,
                LocalDateTime.now(),
                null);
    }

    @Nested
    @DisplayName("Business Methods")
    class BusinessMethods {

        @Test
        @DisplayName("Should activate user")
        void shouldActivateUser() {
            // Given
            user = new UserDomain(1L, "John", "Doe", "johndoe", "john@example.com",
                    "password", "IT", 25, Role.USER, false, LocalDateTime.now(), null);
            assertThat(user.isEnabled()).isFalse();

            // When
            user.activate();

            // Then
            assertThat(user.isEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should deactivate user")
        void shouldDeactivateUser() {
            // Given
            assertThat(user.isEnabled()).isTrue();

            // When
            user.deactivate();

            // Then
            assertThat(user.isEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should allow profile update for same user")
        void shouldAllowProfileUpdateForSameUser() {
            // When
            boolean canUpdate = user.canUpdateProfile(1L);

            // Then
            assertThat(canUpdate).isTrue();
        }

        @Test
        @DisplayName("Should deny profile update for different user")
        void shouldDenyProfileUpdateForDifferentUser() {
            // When
            boolean canUpdate = user.canUpdateProfile(999L);

            // Then
            assertThat(canUpdate).isFalse();
        }

        @Test
        @DisplayName("Should update profile with valid data")
        void shouldUpdateProfileWithValidData() {
            // When
            user.updateProfile("Jane", "Smith", "jane@example.com", "Marketing", 30);

            // Then
            assertThat(user.getFirstname()).isEqualTo("Jane");
            assertThat(user.getLastname()).isEqualTo("Smith");
            assertThat(user.getEmail()).isEqualTo("jane@example.com");
            assertThat(user.getDepartment()).isEqualTo("Marketing");
            assertThat(user.getAge()).isEqualTo(30);
        }

        @Test
        @DisplayName("Should not update with null firstname")
        void shouldNotUpdateWithNullFirstname() {
            // Given
            String originalFirstname = user.getFirstname();

            // When
            user.updateProfile(null, "Smith", "jane@example.com", "Marketing", 30);

            // Then - firstname should remain unchanged
            assertThat(user.getFirstname()).isEqualTo(originalFirstname);
        }

        @Test
        @DisplayName("Should get full name correctly")
        void shouldGetFullNameCorrectly() {
            // When
            String fullName = user.getFullName();

            // Then
            assertThat(fullName).isEqualTo("John Doe");
        }
    }

    @Nested
    @DisplayName("Role Methods")
    class RoleMethods {

        @Test
        @DisplayName("Should identify administrator")
        void shouldIdentifyAdministrator() {
            // Given
            UserDomain admin = new UserDomain(2L, "Admin", "User", "admin", "admin@example.com",
                    "password", "IT", 35, Role.ADMIN, true, LocalDateTime.now(), null);

            // When/Then
            assertThat(admin.isAdministrator()).isTrue();
            assertThat(admin.isRegularUser()).isFalse();
        }

        @Test
        @DisplayName("Should identify regular user")
        void shouldIdentifyRegularUser() {
            // When/Then
            assertThat(user.isRegularUser()).isTrue();
            assertThat(user.isAdministrator()).isFalse();
        }
    }

    @Nested
    @DisplayName("Validation Methods")
    class ValidationMethods {

        @Test
        @DisplayName("Should validate correct email")
        void shouldValidateCorrectEmail() {
            // When/Then
            assertThat(user.hasValidEmail()).isTrue();
        }

        @Test
        @DisplayName("Should invalidate email without @")
        void shouldInvalidateEmailWithoutAt() {
            // Given
            user.setEmail("invalidemail");

            // When/Then
            assertThat(user.hasValidEmail()).isFalse();
        }

        @Test
        @DisplayName("Should validate correct username")
        void shouldValidateCorrectUsername() {
            // When/Then
            assertThat(user.hasValidUsername()).isTrue();
        }

        @Test
        @DisplayName("Should validate correct age")
        void shouldValidateCorrectAge() {
            // When/Then
            assertThat(user.hasValidAge()).isTrue();
        }

        @Test
        @DisplayName("Should invalidate age below 18")
        void shouldInvalidateAgeBelowMinimum() {
            // Given
            UserDomain youngUser = new UserDomain(3L, "Young", "User", "young", "young@example.com",
                    "password", "IT", 16, Role.USER, true, LocalDateTime.now(), null);

            // When/Then
            assertThat(youngUser.hasValidAge()).isFalse();
        }
    }
}
