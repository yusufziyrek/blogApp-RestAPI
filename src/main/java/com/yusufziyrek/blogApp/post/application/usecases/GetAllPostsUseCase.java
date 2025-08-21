package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllPostsUseCase {
    
    private final PostRepository postRepository;
    
    @Cacheable(value = "allPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Post> execute(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}
