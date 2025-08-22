package com.yusufziyrek.blogApp.like.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaLikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserIdAndPostId(Long userId, Long postId);
    Optional<LikeEntity> findByUserIdAndCommentId(Long userId, Long commentId);
    Page<LikeEntity> findByPostId(Long postId, Pageable pageable);
    Page<LikeEntity> findByCommentId(Long commentId, Pageable pageable);
    Page<LikeEntity> findByUserId(Long userId, Pageable pageable);
    long countByPostId(Long postId);
    long countByCommentId(Long commentId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
