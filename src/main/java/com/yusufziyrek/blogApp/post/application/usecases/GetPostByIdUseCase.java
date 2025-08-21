package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostByIdUseCase {
    
    private final PostRepository postRepository;
    
    @Cacheable(value = "posts", key = "#postId")
    public Post execute(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostException("Post not found with id: " + postId));
    }
}
