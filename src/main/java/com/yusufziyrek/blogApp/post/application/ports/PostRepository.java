package com.yusufziyrek.blogApp.post.application.ports;

import com.yusufziyrek.blogApp.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    void deleteById(Long id);
    boolean existsById(Long id);
}
