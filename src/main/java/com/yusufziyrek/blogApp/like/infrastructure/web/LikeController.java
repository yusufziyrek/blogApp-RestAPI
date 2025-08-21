package com.yusufziyrek.blogApp.like.infrastructure.web;

import com.yusufziyrek.blogApp.like.application.usecases.*;
import com.yusufziyrek.blogApp.like.domain.Like;
import com.yusufziyrek.blogApp.like.dto.request.LikeCommentRequest;
import com.yusufziyrek.blogApp.like.dto.request.LikePostRequest;
import com.yusufziyrek.blogApp.like.dto.response.LikeResponse;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.user.domain.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    
    private final LikePostUseCase likePostUseCase;
    private final LikeCommentUseCase likeCommentUseCase;
    private final UnlikeUseCase unlikeUseCase;
    private final GetPostLikesUseCase getPostLikesUseCase;
    private final GetCommentLikesUseCase getCommentLikesUseCase;
    
    @PostMapping("/posts")
    public ResponseEntity<LikeResponse> likePost(
            @Valid @RequestBody LikePostRequest request,
            @AuthenticationPrincipal User user) {
        
        Like like = likePostUseCase.execute(user.getId(), request.getPostId());
        return ResponseEntity.ok(mapToLikeResponse(like));
    }
    
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        
        unlikeUseCase.executeForPost(user.getId(), postId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/comments")
    public ResponseEntity<LikeResponse> likeComment(
            @Valid @RequestBody LikeCommentRequest request,
            @AuthenticationPrincipal User user) {
        
        Like like = likeCommentUseCase.execute(user.getId(), request.getCommentId());
        return ResponseEntity.ok(mapToLikeResponse(like));
    }
    
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> unlikeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user) {
        
        unlikeUseCase.executeForComment(user.getId(), commentId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PageResponse<LikeResponse>> getPostLikes(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Like> likes = getPostLikesUseCase.execute(postId, pageable);
        
        PageResponse<LikeResponse> response = new PageResponse<>();
        response.setItems(likes.getContent().stream()
            .map(this::mapToLikeResponse)
            .toList());
        response.setPage(likes.getNumber());
        response.setSize(likes.getSize());
        response.setTotalItems(likes.getTotalElements());
        response.setTotalPages(likes.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<PageResponse<LikeResponse>> getCommentLikes(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Like> likes = getCommentLikesUseCase.execute(commentId, pageable);
        
        PageResponse<LikeResponse> response = new PageResponse<>();
        response.setItems(likes.getContent().stream()
            .map(this::mapToLikeResponse)
            .toList());
        response.setPage(likes.getNumber());
        response.setSize(likes.getSize());
        response.setTotalItems(likes.getTotalElements());
        response.setTotalPages(likes.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/posts/{postId}/count")
    public ResponseEntity<Long> getPostLikeCount(@PathVariable Long postId) {
        long count = getPostLikesUseCase.getPostLikeCount(postId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/comments/{commentId}/count")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        long count = getCommentLikesUseCase.getCommentLikeCount(commentId);
        return ResponseEntity.ok(count);
    }
    
    private LikeResponse mapToLikeResponse(Like like) {
        LikeResponse response = new LikeResponse();
        response.setId(like.getId());
        response.setUserId(like.getUser().getId());
        response.setUserFullName(like.getUser().getFullName());
        
        if (like.getPost() != null) {
            response.setPostId(like.getPost().getId());
            response.setPostTitle(like.getPost().getTitle());
        }
        
        if (like.getComment() != null) {
            response.setCommentId(like.getComment().getId());
        }
        
        response.setCreatedDate(like.getCreatedDate());
        return response;
    }
}
