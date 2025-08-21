package com.yusufziyrek.blogApp.like.infrastructure.persistence;

import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.like.domain.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    
    private final JpaLikeRepository jpaLikeRepository;
    
    @Override
    public Like save(Like like) {
        return jpaLikeRepository.save(like);
    }
    
    @Override
    public Optional<Like> findById(Long id) {
        return jpaLikeRepository.findById(id);
    }
    
    @Override
    public Optional<Like> findByUserIdAndPostId(Long userId, Long postId) {
        return jpaLikeRepository.findByUserIdAndPostId(userId, postId);
    }
    
    @Override
    public Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId) {
        return jpaLikeRepository.findByUserIdAndCommentId(userId, commentId);
    }
    
    @Override
    public Page<Like> findByPostId(Long postId, Pageable pageable) {
        return jpaLikeRepository.findByPostId(postId, pageable);
    }
    
    @Override
    public Page<Like> findByCommentId(Long commentId, Pageable pageable) {
        return jpaLikeRepository.findByCommentId(commentId, pageable);
    }
    
    @Override
    public Page<Like> findByUserId(Long userId, Pageable pageable) {
        return jpaLikeRepository.findByUserId(userId, pageable);
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
    public void delete(Like like) {
        jpaLikeRepository.delete(like);
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
