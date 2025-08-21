package com.yusufziyrek.blogApp.post.infrastructure.persistence;

import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    
    private final JpaPostRepository jpaPostRepository;
    
    @Override
    public Post save(Post post) {
        return jpaPostRepository.save(post);
    }
    
    @Override
    public Optional<Post> findById(Long id) {
        return jpaPostRepository.findById(id);
    }
    
    @Override
    public Page<Post> findAll(Pageable pageable) {
        return jpaPostRepository.findAll(pageable);
    }
    
    @Override
    public Page<Post> findByUserId(Long userId, Pageable pageable) {
        return jpaPostRepository.findByUserId(userId, pageable);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaPostRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaPostRepository.existsById(id);
    }
}
