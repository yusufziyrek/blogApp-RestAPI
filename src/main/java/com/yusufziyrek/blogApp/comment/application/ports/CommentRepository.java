package com.yusufziyrek.blogApp.comment.application.ports;

import com.yusufziyrek.blogApp.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findByPostId(Long postId);
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    void deleteById(Long id);
    boolean existsById(Long id);
    long countByPostId(Long postId);
}
