package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import java.util.List;

public class GetPostsByUserIdUseCaseImpl implements GetPostsByUserIdUseCase {
    
    private final PostRepository postRepository;
    
    public GetPostsByUserIdUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public PageResponse<PostDomain> execute(Long userId, int page, int size) {
        List<PostDomain> posts = postRepository.findByUserId(userId, page, size);
        long totalCount = postRepository.getTotalCountByUserId(userId);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        return new PageResponse<>(posts, page, size, totalCount, totalPages);
    }
}
