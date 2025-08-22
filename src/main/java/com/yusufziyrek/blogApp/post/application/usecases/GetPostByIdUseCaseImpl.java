package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostByIdUseCaseImpl implements GetPostByIdUseCase {
    
    private final PostRepository postRepository;
    
    @Override
    @Cacheable(value = "posts", key = "#postId")
    public PostDomain execute(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostException("PostDomain not found with id: " + postId));
    }
}
