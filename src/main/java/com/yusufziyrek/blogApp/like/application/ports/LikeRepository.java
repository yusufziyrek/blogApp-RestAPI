package com.yusufziyrek.blogApp.like.application.ports;

import com.yusufziyrek.blogApp.like.domain.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LikeRepository {
    Like save(Like like);
    Optional<Like> findById(Long id);
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);
    Page<Like> findByPostId(Long postId, Pageable pageable);
    Page<Like> findByCommentId(Long commentId, Pageable pageable);
    Page<Like> findByUserId(Long userId, Pageable pageable);
    long countByPostId(Long postId);
    long countByCommentId(Long commentId);
    void delete(Like like);
    void deleteById(Long id);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
