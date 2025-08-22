package com.yusufziyrek.blogApp.like.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.infrastructure.persistence.LikeEntity;

@Component
public class LikeMapper {
    
    public LikeDomain toDomain(LikeEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new LikeDomain(
            entity.getId(),
            entity.getUser().getId(),
            entity.getPost() != null ? entity.getPost().getId() : null,
            entity.getComment() != null ? entity.getComment().getId() : null,
            entity.getCreatedAt()
        );
    }
    
    public LikeEntity toEntity(LikeDomain domain) {
        if (domain == null) {
            return null;
        }
        
        LikeEntity entity = new LikeEntity();
        entity.setId(domain.getId());
        entity.setCreatedAt(domain.getCreatedDate());
        
    // Not: User/Post/Comment entity'lerini repository kaydederken setleyecek
    // (bunlar ilgili repository'lerden yüklenmeli)
        
        return entity;
    }
}
