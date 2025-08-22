package com.yusufziyrek.blogApp.auth.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.auth.infrastructure.persistence.RefreshTokenEntity;

@Component
public class RefreshTokenMapper {
    
    public RefreshTokenDomain toDomain(RefreshTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new RefreshTokenDomain(
            entity.getId(),
            entity.getToken(),
            entity.getUser().getId(),
            entity.getExpiresAt(),
            entity.getIsRevoked(),
            entity.getCreatedDate()
        );
    }
    
    public RefreshTokenEntity toEntity(RefreshTokenDomain domain) {
        if (domain == null) {
            return null;
        }
        
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setIsRevoked(domain.getIsRevoked());
        entity.setCreatedDate(domain.getCreatedDate());
        
    // Not: UserEntity kaydederken repository tarafından setlenecek
    // (UserRepository'den yüklenmesi gerekiyor)
        
        return entity;
    }
}
