package com.yusufziyrek.blogApp.comment.application.usecases.impl;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentsForPostUseCase;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import java.util.List;

public class GetCommentsForPostUseCaseImpl implements GetCommentsForPostUseCase {
    
    private final CommentRepository commentRepository;
    
    public GetCommentsForPostUseCaseImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    @Override
    public PageResponse<CommentDomain> execute(Long postId, int page, int size) {
        List<CommentDomain> comments = commentRepository.findByPostId(postId, page, size);
        long totalCount = commentRepository.getTotalCountByPostId(postId);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        return new PageResponse<>(comments, page, size, totalCount, totalPages);
    }
}
