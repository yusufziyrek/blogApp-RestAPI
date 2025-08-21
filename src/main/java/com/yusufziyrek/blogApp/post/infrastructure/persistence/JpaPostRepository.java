package com.yusufziyrek.blogApp.post.infrastructure.persistence;

import com.yusufziyrek.blogApp.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUserId(Long userId, Pageable pageable);
}
