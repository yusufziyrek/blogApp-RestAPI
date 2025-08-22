package com.yusufziyrek.blogApp.comment.application.ports;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import java.util.List;
import java.util.Optional;

/**
 * Port (Interface) for CommentDomain Repository
 * No framework dependencies - Pure domain interface
 */
public interface CommentRepository {
    CommentDomain save(CommentDomain comment);
    Optional<CommentDomain> findById(Long id);
    List<CommentDomain> findByPostId(Long postId, int page, int size);
    List<CommentDomain> findByUserId(Long userId, int page, int size);
    void deleteById(Long id);
    boolean existsById(Long id);
    long getTotalCountByPostId(Long postId);
    long getTotalCountByUserId(Long userId);
    long getTotalCount();
}
