package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import java.util.List;

public class GetAllPostsUseCaseImpl implements GetAllPostsUseCase {
    
    private final PostRepository postRepository;
    
    public GetAllPostsUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public PageResponse<PostDomain> execute(int page, int size) {
        List<PostDomain> posts = postRepository.findAll(page, size);
        long totalCount = postRepository.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        return new PageResponse<>(posts, page, size, totalCount, totalPages);
    }
}
