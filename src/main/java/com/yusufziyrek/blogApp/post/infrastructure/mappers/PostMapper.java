package com.yusufziyrek.blogApp.post.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.PostEntity;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;

/**
 * PostDomain ile PostEntity arası mapper
 * Dönüşümleri yapıyor, sorumlulukları ayırıyorum
 */
@Component
public class PostMapper {
    
    /**
    * PostDomain -> PostEntity (persist için)
     */
    public PostEntity toEntity(PostDomain domain, UserEntity userEntity) {
        if (domain == null) {
            return null;
        }
        
        PostEntity entity = new PostEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setText(domain.getText());
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setUpdatedDate(domain.getUpdatedDate());
        entity.setCommentCount(domain.getCommentCount());
        entity.setLikeCount(domain.getLikeCount());
        entity.setUser(userEntity);
        
        return entity;
    }
    
    /**
    * PostEntity -> PostDomain (iş mantığı için)
     */
    public PostDomain toDomain(PostEntity entity) {
        if (entity == null) {
            return null;
        }
        
    // Var olan post'lar için uygun constructor ile domain nesnesi oluşturuyorum
        return new PostDomain(
            entity.getId(),
            entity.getTitle(),
            entity.getText(),
            entity.getCreatedDate(),
            entity.getUpdatedDate(),
            entity.getCommentCount(),
            entity.getLikeCount(),
            entity.getUser().getId()
        );
    }
    
    /**
    * PostDomain -> PostResponse (API cevabı için)
     */
    public com.yusufziyrek.blogApp.post.dto.response.PostResponse toResponse(PostDomain domain, String authorUsername, String authorFullName) {
        if (domain == null) {
            return null;
        }
        
        com.yusufziyrek.blogApp.post.dto.response.PostResponse response = 
            new com.yusufziyrek.blogApp.post.dto.response.PostResponse();
        
        response.setId(domain.getId());
        response.setTitle(domain.getTitle());
        response.setText(domain.getText());
        response.setCreatedDate(domain.getCreatedDate());
        response.setUpdatedDate(domain.getUpdatedDate());
        response.setCommentCount(domain.getCommentCount());
        response.setLikeCount(domain.getLikeCount());
        response.setAuthorUsername(authorUsername);
        response.setAuthorFullName(authorFullName);
        
        return response;
    }
}
