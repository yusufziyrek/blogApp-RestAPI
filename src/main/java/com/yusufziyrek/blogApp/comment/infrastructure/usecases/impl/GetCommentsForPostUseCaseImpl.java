package com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl;

import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentsForPostUseCase;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCommentsForPostUseCaseImpl implements GetCommentsForPostUseCase {
    
    private final CommentRepository commentRepository;
    
    @Override
    public PageResponse<CommentDomain> execute(Long postId, int page, int size) {
        List<CommentDomain> comments = commentRepository.findByPostId(postId, page, size);
        long totalCount = commentRepository.getTotalCountByPostId(postId);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        return new PageResponse<>(comments, page, size, totalCount, totalPages);
    }
}
