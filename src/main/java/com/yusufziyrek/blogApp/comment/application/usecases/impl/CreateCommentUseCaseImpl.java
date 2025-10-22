package com.yusufziyrek.blogApp.comment.application.usecases.impl;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.CreateCommentUseCase;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.dto.request.CreateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

public class CreateCommentUseCaseImpl implements CreateCommentUseCase {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public CreateCommentUseCaseImpl(CommentRepository commentRepository, 
                                    PostRepository postRepository, 
                                    UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public CommentResponse execute(CreateCommentRequest request, Long userId) {
    // Kullanıcının varlığını doğrula
    UserDomain user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, userId), HttpStatus.NOT_FOUND));
        
        // Postun varlığını doğrula
        PostDomain post = postRepository.findById(request.getPostId())
        .orElseThrow(() -> new PostException(ErrorMessages.POST_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        try {
            // Comment domain nesnesini oluştur
            CommentDomain comment = new CommentDomain(
                request.getText(),
                request.getPostId(),
                userId
            );
            
            // Yorum kaydet
            CommentDomain savedComment = commentRepository.save(comment);
            
            // Postun yorum sayısını domain mantığı ile güncelle
            post.incrementCommentCount();
            postRepository.save(post);
            
            // Response'a dönüştür
            CommentResponse response = new CommentResponse();
            response.setId(savedComment.getId());
            response.setText(savedComment.getText());
            response.setLikeCount(savedComment.getLikeCount());
            response.setCreatedDate(savedComment.getCreatedDate());
            response.setUpdatedDate(savedComment.getUpdatedDate());
            response.setPostId(savedComment.getPostId());
            response.setPostTitle(post.getTitle());
            response.setAuthorUsername(user.getUsername());
            response.setAuthorFullName(user.getFirstname() + " " + user.getLastname());
            
            return response;
            
        } catch (IllegalArgumentException e) {
            throw new CommentException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
