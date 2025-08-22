package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.dto.request.CreatePostRequest;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreatePostUseCaseImpl implements CreatePostUseCase {
    
    private final PostRepository postRepository;
    
    @Override
    @CacheEvict(value = "allPosts", allEntries = true)
    public PostDomain execute(CreatePostRequest request, Long userId) {
        log.info("Creating new post with title: '{}' by user ID: {}", request.getTitle(), userId);
        
    // Aynı başlık var mı kontrol et
        if (postRepository.existsByTitle(request.getTitle())) {
            log.warn("PostDomain creation failed - title already exists: {}", request.getTitle());
            throw new PostException("A post with this title already exists");
        }
        
        try {
            // Domain nesnesini oluştur
            PostDomain post = new PostDomain(
                request.getTitle(),
                request.getText(),
                userId
            );
            
            // Post'u kaydet
            PostDomain savedPost = postRepository.save(post);
            log.info("PostDomain created successfully with ID: {} and title: '{}'", savedPost.getId(), savedPost.getTitle());
            
            return savedPost;
                
        } catch (IllegalArgumentException e) {
            log.error("PostDomain validation failed during creation: {}", e.getMessage());
            throw new PostException(e.getMessage());
        }
    }
}
