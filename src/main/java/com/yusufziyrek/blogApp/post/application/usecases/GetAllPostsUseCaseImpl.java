package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllPostsUseCaseImpl implements GetAllPostsUseCase {
    
    private final PostRepository postRepository;
    
    @Override
    @Cacheable(value = "allPosts", key = "#page + '-' + #size")
    public PageResponse<PostDomain> execute(int page, int size) {
        List<PostDomain> posts = postRepository.findAll(page, size);
        long totalCount = postRepository.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        return new PageResponse<>(posts, page, size, totalCount, totalPages);
    }
}
