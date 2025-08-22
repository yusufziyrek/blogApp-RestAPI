package com.yusufziyrek.blogApp.user.infrastructure.mappers;

import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.RoleEntity;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;

/**
 * Mapper between Domain and Infrastructure layers
 * Clean Architecture - Infrastructure Layer
 */
public class UserMapper {
    
    /**
     * Maps Domain entity to JPA entity
     */
    public static UserEntity toEntity(UserDomain domain) {
        if (domain == null) {
            return null;
        }
        
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setFirstname(domain.getFirstname());
        entity.setLastname(domain.getLastname());
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setDepartment(domain.getDepartment());
        entity.setAge(domain.getAge());
        entity.setRole(toRoleEntity(domain.getRole()));
        entity.setEnabled(domain.isEnabled());
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setUpdatedDate(domain.getUpdatedDate());
        
        return entity;
    }
    
    /**
     * Maps JPA entity to Domain entity
     */
    public static UserDomain toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new UserDomain(
            entity.getId(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getDepartment(),
            entity.getAge(),
            toRole(entity.getRole()),
            entity.isEnabled(),
            entity.getCreatedDate(),
            entity.getUpdatedDate()
        );
    }
    
    /**
     * Maps Domain Role to JPA Role Entity
     */
    public static RoleEntity toRoleEntity(Role role) {
        if (role == null) {
            return null;
        }
        
        return switch (role) {
            case USER -> RoleEntity.USER;
            case ADMIN -> RoleEntity.ADMIN;
        };
    }
    
    /**
     * Maps JPA Role Entity to Domain Role
     */
    public static Role toRole(RoleEntity roleEntity) {
        if (roleEntity == null) {
            return null;
        }
        
        return switch (roleEntity) {
            case USER -> Role.USER;
            case ADMIN -> Role.ADMIN;
        };
    }
    
    /**
     * Maps Domain to Response DTO
     */
    public static UserResponse toResponse(UserDomain domain) {
        if (domain == null) {
            return null;
        }
        
        return new UserResponse(
            domain.getId(),
            domain.getFirstname(),
            domain.getLastname(),
            domain.getUsername(),
            domain.getEmail(),
            domain.getDepartment(),
            domain.getAge(),
            domain.getRole(),
            domain.isEnabled(),
            domain.getCreatedDate(),
            domain.getUpdatedDate()
        );
    }
}
