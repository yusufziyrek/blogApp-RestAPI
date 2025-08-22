package com.yusufziyrek.blogApp.like.application.ports;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LikeRepository {
    LikeDomain save(LikeDomain like);
    Optional<LikeDomain> findById(Long id);
    Optional<LikeDomain> findByUserIdAndPostId(Long userId, Long postId);
    Optional<LikeDomain> findByUserIdAndCommentId(Long userId, Long commentId);
    Page<LikeDomain> findByPostId(Long postId, Pageable pageable);
    Page<LikeDomain> findByCommentId(Long commentId, Pageable pageable);
    Page<LikeDomain> findByUserId(Long userId, Pageable pageable);
    long countByPostId(Long postId);
    long countByCommentId(Long commentId);
    void delete(LikeDomain like);
    void deleteById(Long id);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
