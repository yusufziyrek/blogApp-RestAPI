package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.exception.PostException;

public class GetPostByIdUseCaseImpl implements GetPostByIdUseCase {
    
    private final PostRepository postRepository;
    
    public GetPostByIdUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public PostDomain execute(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostException("PostDomain not found with id: " + postId));
    }
}
