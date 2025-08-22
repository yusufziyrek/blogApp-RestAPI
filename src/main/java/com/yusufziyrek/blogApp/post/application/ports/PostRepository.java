package com.yusufziyrek.blogApp.post.application.ports;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import java.util.List;
import java.util.Optional;

/**
 * Port (Interface) for PostDomain Repository
 * No framework dependencies - Pure domain interface
 */
public interface PostRepository {
    PostDomain save(PostDomain post);
    Optional<PostDomain> findById(Long id);
    List<PostDomain> findAll(int page, int size);
    List<PostDomain> findByUserId(Long userId, int page, int size);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, Long id);
    long getTotalCount();
    long getTotalCountByUserId(Long userId);
}
