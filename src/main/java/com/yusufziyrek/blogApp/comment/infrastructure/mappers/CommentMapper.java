package com.yusufziyrek.blogApp.comment.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.infrastructure.persistence.CommentEntity;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.PostEntity;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;

/**
 * Mapper between CommentDomain (pure domain) and CommentEntity (JPA infrastructure)
 * Handles conversion and maintains separation of concerns
 */
@Component
public class CommentMapper {
    
    /**
     * Convert CommentDomain to CommentEntity for persistence
     */
    public CommentEntity toEntity(CommentDomain domain, PostEntity postEntity, UserEntity userEntity) {
        if (domain == null) {
            return null;
        }
        
        CommentEntity entity = new CommentEntity();
        entity.setId(domain.getId());
        entity.setText(domain.getText());
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setUpdatedDate(domain.getUpdatedDate());
        entity.setPost(postEntity);
        entity.setUser(userEntity);
        
        return entity;
    }
    
    /**
     * Convert CommentEntity to CommentDomain for business logic
     */
    public CommentDomain toDomain(CommentEntity entity) {
        if (entity == null) {
            return null;
        }
        
    // Var olan yorumlar için uygun constructor ile domain nesnesi oluşturuyorum
        return new CommentDomain(
            entity.getId(),
            entity.getText(),
            entity.getCreatedDate(),
            entity.getUpdatedDate(),
            entity.getPost().getId(),
            entity.getUser().getId()
        );
    }
}
