package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostsByUserIdUseCaseImpl implements GetPostsByUserIdUseCase {
    
    private final PostRepository postRepository;
    
    @Override
    public PageResponse<PostDomain> execute(Long userId, int page, int size) {
        List<PostDomain> posts = postRepository.findByUserId(userId, page, size);
        long totalCount = postRepository.getTotalCountByUserId(userId);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        return new PageResponse<>(posts, page, size, totalCount, totalPages);
    }
}
