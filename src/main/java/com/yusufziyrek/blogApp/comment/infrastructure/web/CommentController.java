package com.yusufziyrek.blogApp.comment.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.comment.application.services.CommentApplicationService;
import com.yusufziyrek.blogApp.comment.dto.request.UpdateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.like.dto.response.LikeResponse;
import com.yusufziyrek.blogApp.shared.security.UserPrincipal;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentApplicationService commentApplicationService;
    
    // Yorumların POST/GET operasyonlarını artık PostController içinde yönetiyorum
    // Endpoint: /api/v1/posts/{postId}/comments
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> getCommentById(@PathVariable Long id) {
        CommentResponse response = commentApplicationService.getCommentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        CommentResponse response = commentApplicationService.updateComment(id, request, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        
        commentApplicationService.deleteComment(id, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment deleted successfully", null));
    }
    
    @PostMapping("/{commentId}/likes")
    public ResponseEntity<ApiResponse<LikeResponse>> likeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal user) {
        
        LikeResponse response = commentApplicationService.likeComment(commentId, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment liked successfully", response));
    }
    
    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<ApiResponse<Void>> unlikeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal user) {
        
        commentApplicationService.unlikeComment(commentId, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment unliked successfully", null));
    }
    
    @GetMapping("/{commentId}/likes")
    public ResponseEntity<ApiResponse<List<LikeResponse>>> getCommentLikes(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<LikeResponse> responses = commentApplicationService.getCommentLikes(commentId, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment likes retrieved successfully", responses));
    }
}
