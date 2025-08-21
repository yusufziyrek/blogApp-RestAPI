package com.yusufziyrek.blogApp.like.infrastructure.persistence;

import com.yusufziyrek.blogApp.like.domain.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaLikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);
    Page<Like> findByPostId(Long postId, Pageable pageable);
    Page<Like> findByCommentId(Long commentId, Pageable pageable);
    Page<Like> findByUserId(Long userId, Pageable pageable);
    long countByPostId(Long postId);
    long countByCommentId(Long commentId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
