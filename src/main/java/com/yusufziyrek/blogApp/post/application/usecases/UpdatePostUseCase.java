package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UpdatePostUseCase {
    
    private final PostRepository postRepository;
    
    @CacheEvict(value = "allPosts", allEntries = true)
    public Post execute(Long postId, String title, String text, Long userId) {
        // Find post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found"));
        
        // Check if user can edit this post (domain logic)
        if (!post.canBeEditedBy(userId)) {
            throw new PostException("You are not authorized to edit this post");
        }
        
        // Update using domain logic
        post.updateContent(title, text);
        
        return postRepository.save(post);
    }
}
