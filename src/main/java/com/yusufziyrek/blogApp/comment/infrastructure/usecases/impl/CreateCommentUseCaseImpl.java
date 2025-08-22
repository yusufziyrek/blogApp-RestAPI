package com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.CreateCommentUseCase;
import com.yusufziyrek.blogApp.comment.dto.request.CreateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreateCommentUseCaseImpl implements CreateCommentUseCase {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Override
    public CommentResponse execute(CreateCommentRequest request, Long userId) {
        log.info("Creating new comment for post ID: {} by user ID: {}", request.getPostId(), userId);
        
    // Kullanıcının varlığını doğrula
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException("UserDomain not found"));
        
    // Postun varlığını doğrula
        PostDomain post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostException("PostDomain not found"));
        
        try {
            // Comment domain nesnesini oluştur
            CommentDomain comment = new CommentDomain(
                request.getText(),
                request.getPostId(),
                userId
            );
            
            // Save comment
                // Yorum kaydet
            CommentDomain savedComment = commentRepository.save(comment);
            
            // Postun yorum sayısını domain mantığı ile güncelle
            post.incrementCommentCount();
            postRepository.save(post);
            
            log.info("CommentDomain created successfully with ID: {} for post ID: {}", savedComment.getId(), request.getPostId());
            
            // Response'a dönüştür
            CommentResponse response = new CommentResponse();
            response.setId(savedComment.getId());
            response.setText(savedComment.getText());
            response.setCreatedDate(savedComment.getCreatedDate());
            response.setUpdatedDate(savedComment.getUpdatedDate());
            response.setPostId(savedComment.getPostId());
            // Not: Yazar bilgileri controller katmanında setlenecek
            
            return response;
            
        } catch (IllegalArgumentException e) {
            log.error("CommentDomain validation failed: {}", e.getMessage());
            throw new CommentException(e.getMessage());
        }
    }
}
