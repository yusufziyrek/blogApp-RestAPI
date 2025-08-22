package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UpdatePostUseCaseImpl implements UpdatePostUseCase {
    
    private final PostRepository postRepository;
    
    @Override
    @CacheEvict(value = "allPosts", allEntries = true)
    public PostDomain execute(Long postId, String title, String text, Long userId) {
        log.info("Updating post ID: {} with title: '{}' by user ID: {}", postId, title, userId);
        
    // Post'u bul
        PostDomain post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("PostDomain not found"));
        
    // Kullanıcı bu postu düzenleyebilir mi kontrol et (domain mantığı)
        if (!post.canBeEditedBy(userId)) {
            log.warn("Unauthorized post update attempt by user ID: {} for post ID: {}", userId, postId);
            throw new PostException("You are not authorized to edit this post");
        }
        
    // Başlık değişiyorsa, aynı başlık var mı kontrol et
        if (title != null && !title.equals(post.getTitle()) && postRepository.existsByTitleAndIdNot(title, postId)) {
            log.warn("PostDomain update failed - title already exists: {}", title);
            throw new PostException("A post with this title already exists");
        }
        
        try {
            // Domain mantığı ile güncelle
            post.updateContent(title, text);
            
            PostDomain updatedPost = postRepository.save(post);
            log.info("PostDomain updated successfully with ID: {} and title: '{}'", updatedPost.getId(), updatedPost.getTitle());
            return updatedPost;
        } catch (IllegalArgumentException e) {
            log.error("PostDomain validation failed during update: {}", e.getMessage());
            throw new PostException(e.getMessage());
        }
    }
}
