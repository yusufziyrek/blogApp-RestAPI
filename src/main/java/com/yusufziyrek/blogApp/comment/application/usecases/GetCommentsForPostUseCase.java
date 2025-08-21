package com.yusufziyrek.blogApp.comment.application.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCommentsForPostUseCase {
    
    private final CommentRepository commentRepository;
    
    public Page<Comment> execute(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }
}
