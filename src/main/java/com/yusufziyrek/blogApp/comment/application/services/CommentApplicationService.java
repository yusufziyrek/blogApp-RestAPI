package com.yusufziyrek.blogApp.comment.application.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.comment.application.usecases.*;
import com.yusufziyrek.blogApp.like.application.usecases.LikeCommentUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.UnlikeUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.GetCommentLikesUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByIdUseCase;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.dto.request.UpdateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.dto.response.LikeResponse;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CommentApplicationService {
    
    // Comment related use cases
    private final GetCommentByIdUseCase getCommentByIdUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    
    // Cross-module use cases
    private final LikeCommentUseCase likeCommentUseCase;
    private final UnlikeUseCase unlikeUseCase;
    private final GetCommentLikesUseCase getCommentLikesUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    
    public CommentApplicationService(GetCommentByIdUseCase getCommentByIdUseCase, UpdateCommentUseCase updateCommentUseCase,
                                   DeleteCommentUseCase deleteCommentUseCase, LikeCommentUseCase likeCommentUseCase,
                                   UnlikeUseCase unlikeUseCase, GetCommentLikesUseCase getCommentLikesUseCase,
                                   GetUserByIdUseCase getUserByIdUseCase) {
        this.getCommentByIdUseCase = getCommentByIdUseCase;
        this.updateCommentUseCase = updateCommentUseCase;
        this.deleteCommentUseCase = deleteCommentUseCase;
        this.likeCommentUseCase = likeCommentUseCase;
        this.unlikeUseCase = unlikeUseCase;
        this.getCommentLikesUseCase = getCommentLikesUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
    }
    
    public CommentResponse getCommentById(Long id) {
        CommentDomain comment = getCommentByIdUseCase.execute(id);
        return enrichCommentResponse(comment);
    }
    
    public CommentResponse updateComment(Long id, UpdateCommentRequest request, Long userId) {
        CommentDomain updatedComment = updateCommentUseCase.execute(id, request.getText(), userId);
        return enrichCommentResponse(updatedComment);
    }
    
    public void deleteComment(Long id, Long userId) {
        deleteCommentUseCase.execute(id, userId);
    }
    
    public LikeResponse likeComment(Long commentId, Long userId) {
        LikeDomain like = likeCommentUseCase.execute(userId, commentId);
        return mapToLikeResponse(like);
    }
    
    public void unlikeComment(Long commentId, Long userId) {
        unlikeUseCase.executeForComment(userId, commentId);
    }
    
    public List<LikeResponse> getCommentLikes(Long commentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LikeDomain> likes = getCommentLikesUseCase.execute(commentId, pageable);
        
        return likes.getContent().stream()
                .map(this::mapToLikeResponse)
                .collect(Collectors.toList());
    }
    
    private CommentResponse enrichCommentResponse(CommentDomain comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        response.setPostId(comment.getPostId());
        
        // Cross-module data enrichment
        try {
            UserResponse user = getUserByIdUseCase.execute(comment.getUserId());
            response.setAuthorUsername(user.getUsername());
            response.setAuthorFullName(user.getFirstname() + " " + user.getLastname());
        } catch (Exception e) {
            response.setAuthorUsername("Unknown");
            response.setAuthorFullName("Unknown User");
        }
        
        return response;
    }
    
    private LikeResponse mapToLikeResponse(LikeDomain like) {
        LikeResponse response = new LikeResponse();
        response.setId(like.getId());
        response.setUserId(like.getUserId());
        response.setPostId(like.getPostId());
        response.setCommentId(like.getCommentId());
        response.setCreatedDate(like.getCreatedDate());
        
        // Cross-module data enrichment
        try {
            UserResponse user = getUserByIdUseCase.execute(like.getUserId());
            response.setUserFullName(user.getFirstname() + " " + user.getLastname());
        } catch (Exception e) {
            response.setUserFullName("Unknown User");
        }
        
        return response;
    }
}
