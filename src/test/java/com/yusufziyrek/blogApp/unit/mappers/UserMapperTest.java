package com.yusufziyrek.blogApp.unit.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.user.infrastructure.mappers.UserMapper;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.RoleEntity;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;

/**
 * Unit tests for UserMapper
 * Tests mapping between Domain, Entity, and Response
 */
@DisplayName("UserMapper Tests")
class UserMapperTest {

    @Nested
    @DisplayName("toEntity Method")
    class ToEntityMethod {

        @Test
        @DisplayName("Should map domain to entity correctly")
        void shouldMapDomainToEntityCorrectly() {
            // Given
            UserDomain domain = createUserDomain(1L, "test@example.com", Role.USER);

            // When
            UserEntity entity = UserMapper.toEntity(domain);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getEmail()).isEqualTo("test@example.com");
            assertThat(entity.getUsername()).isEqualTo("testuser");
            assertThat(entity.getFirstname()).isEqualTo("Test");
            assertThat(entity.getLastname()).isEqualTo("User");
            assertThat(entity.getRole()).isEqualTo(RoleEntity.USER);
        }

        @Test
        @DisplayName("Should return null for null domain")
        void shouldReturnNullForNullDomain() {
            // When
            UserEntity entity = UserMapper.toEntity(null);

            // Then
            assertThat(entity).isNull();
        }

        @Test
        @DisplayName("Should map admin role correctly")
        void shouldMapAdminRoleCorrectly() {
            // Given
            UserDomain domain = createUserDomain(2L, "admin@example.com", Role.ADMIN);

            // When
            UserEntity entity = UserMapper.toEntity(domain);

            // Then
            assertThat(entity.getRole()).isEqualTo(RoleEntity.ADMIN);
        }
    }

    @Nested
    @DisplayName("toDomain Method")
    class ToDomainMethod {

        @Test
        @DisplayName("Should map entity to domain correctly")
        void shouldMapEntityToDomainCorrectly() {
            // Given
            UserEntity entity = createUserEntity(1L, "test@example.com", RoleEntity.USER);

            // When
            UserDomain domain = UserMapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isEqualTo(1L);
            assertThat(domain.getEmail()).isEqualTo("test@example.com");
            assertThat(domain.getUsername()).isEqualTo("testuser");
            assertThat(domain.getRole()).isEqualTo(Role.USER);
        }

        @Test
        @DisplayName("Should return null for null entity")
        void shouldReturnNullForNullEntity() {
            // When
            UserDomain domain = UserMapper.toDomain(null);

            // Then
            assertThat(domain).isNull();
        }
    }

    @Nested
    @DisplayName("toResponse Method")
    class ToResponseMethod {

        @Test
        @DisplayName("Should map domain to response correctly")
        void shouldMapDomainToResponseCorrectly() {
            // Given
            UserDomain domain = createUserDomain(1L, "test@example.com", Role.USER);

            // When
            UserResponse response = UserMapper.toResponse(domain);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getEmail()).isEqualTo("test@example.com");
            assertThat(response.getUsername()).isEqualTo("testuser");
            assertThat(response.getFirstname()).isEqualTo("Test");
            assertThat(response.getLastname()).isEqualTo("User");
            assertThat(response.getRole()).isEqualTo(Role.USER);
        }

        @Test
        @DisplayName("Should return null for null domain")
        void shouldReturnNullForNullDomain() {
            // When
            UserResponse response = UserMapper.toResponse(null);

            // Then
            assertThat(response).isNull();
        }
    }

    @Nested
    @DisplayName("Role Mapping")
    class RoleMapping {

        @Test
        @DisplayName("Should map USER role to RoleEntity.USER")
        void shouldMapUserRoleToRoleEntityUser() {
            // When
            RoleEntity roleEntity = UserMapper.toRoleEntity(Role.USER);

            // Then
            assertThat(roleEntity).isEqualTo(RoleEntity.USER);
        }

        @Test
        @DisplayName("Should map ADMIN role to RoleEntity.ADMIN")
        void shouldMapAdminRoleToRoleEntityAdmin() {
            // When
            RoleEntity roleEntity = UserMapper.toRoleEntity(Role.ADMIN);

            // Then
            assertThat(roleEntity).isEqualTo(RoleEntity.ADMIN);
        }

        @Test
        @DisplayName("Should map RoleEntity.USER to USER role")
        void shouldMapRoleEntityUserToUserRole() {
            // When
            Role role = UserMapper.toRole(RoleEntity.USER);

            // Then
            assertThat(role).isEqualTo(Role.USER);
        }

        @Test
        @DisplayName("Should map RoleEntity.ADMIN to ADMIN role")
        void shouldMapRoleEntityAdminToAdminRole() {
            // When
            Role role = UserMapper.toRole(RoleEntity.ADMIN);

            // Then
            assertThat(role).isEqualTo(Role.ADMIN);
        }

        @Test
        @DisplayName("Should return null for null role")
        void shouldReturnNullForNullRole() {
            // When
            RoleEntity roleEntity = UserMapper.toRoleEntity(null);
            Role role = UserMapper.toRole(null);

            // Then
            assertThat(roleEntity).isNull();
            assertThat(role).isNull();
        }
    }

    // Helper methods
    private UserDomain createUserDomain(Long id, String email, Role role) {
        return new UserDomain(
                id,
                "Test",
                "User",
                "testuser",
                email,
                "hashedPassword",
                "IT",
                25,
                role,
                true,
                LocalDateTime.now(),
                null);
    }

    private UserEntity createUserEntity(Long id, String email, RoleEntity role) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setFirstname("Test");
        entity.setLastname("User");
        entity.setUsername("testuser");
        entity.setEmail(email);
        entity.setPassword("hashedPassword");
        entity.setDepartment("IT");
        entity.setAge(25);
        entity.setRole(role);
        entity.setEnabled(true);
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }
}
