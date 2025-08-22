package com.yusufziyrek.blogApp.comment.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostId(Long postId);
    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
    Page<CommentEntity> findByUserId(Long userId, Pageable pageable);
    long countByPostId(Long postId);
}
