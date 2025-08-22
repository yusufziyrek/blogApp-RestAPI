package com.yusufziyrek.blogApp.like.infrastructure.persistence;

import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.infrastructure.mappers.LikeMapper;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.PostEntity;
import com.yusufziyrek.blogApp.comment.infrastructure.persistence.CommentEntity;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.JpaUserRepository;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.JpaPostRepository;
import com.yusufziyrek.blogApp.comment.infrastructure.persistence.JpaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    
    private final JpaLikeRepository jpaLikeRepository;
    private final LikeMapper likeMapper;
    private final JpaUserRepository jpaUserRepository;
    private final JpaPostRepository jpaPostRepository;
    private final JpaCommentRepository jpaCommentRepository;
    
    @Override
    public LikeDomain save(LikeDomain like) {
        LikeEntity entity = likeMapper.toEntity(like);
        
    // İlgili referansları yükleyip setliyorum
        UserEntity user = jpaUserRepository.findById(like.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        entity.setUser(user);
        
        if (like.getPostId() != null) {
            PostEntity post = jpaPostRepository.findById(like.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
            entity.setPost(post);
        }
        
        if (like.getCommentId() != null) {
            CommentEntity comment = jpaCommentRepository.findById(like.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
            entity.setComment(comment);
        }
        
        LikeEntity savedEntity = jpaLikeRepository.save(entity);
        return likeMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<LikeDomain> findById(Long id) {
        return jpaLikeRepository.findById(id)
            .map(likeMapper::toDomain);
    }
    
    @Override
    public Optional<LikeDomain> findByUserIdAndPostId(Long userId, Long postId) {
        return jpaLikeRepository.findByUserIdAndPostId(userId, postId)
            .map(likeMapper::toDomain);
    }
    
    @Override
    public Optional<LikeDomain> findByUserIdAndCommentId(Long userId, Long commentId) {
        return jpaLikeRepository.findByUserIdAndCommentId(userId, commentId)
            .map(likeMapper::toDomain);
    }
    
    @Override
    public Page<LikeDomain> findByPostId(Long postId, Pageable pageable) {
        return jpaLikeRepository.findByPostId(postId, pageable)
            .map(likeMapper::toDomain);
    }
    
    @Override
    public Page<LikeDomain> findByCommentId(Long commentId, Pageable pageable) {
        return jpaLikeRepository.findByCommentId(commentId, pageable)
            .map(likeMapper::toDomain);
    }
    
    @Override
    public Page<LikeDomain> findByUserId(Long userId, Pageable pageable) {
        return jpaLikeRepository.findByUserId(userId, pageable)
            .map(likeMapper::toDomain);
    }
    
    @Override
    public long countByPostId(Long postId) {
        return jpaLikeRepository.countByPostId(postId);
    }
    
    @Override
    public long countByCommentId(Long commentId) {
        return jpaLikeRepository.countByCommentId(commentId);
    }
    
    @Override
    public void delete(LikeDomain like) {
        LikeEntity entity = likeMapper.toEntity(like);
        jpaLikeRepository.delete(entity);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaLikeRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByUserIdAndPostId(Long userId, Long postId) {
        return jpaLikeRepository.existsByUserIdAndPostId(userId, postId);
    }
    
    @Override
    public boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
        return jpaLikeRepository.existsByUserIdAndCommentId(userId, commentId);
    }
}
