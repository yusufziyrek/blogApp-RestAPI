package com.yusufziyrek.blogApp.comment.infrastructure.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentByIdUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.UpdateCommentUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.DeleteCommentUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.LikeCommentUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.UnlikeUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.GetCommentLikesUseCase;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.dto.request.UpdateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.dto.response.LikeResponse;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final GetCommentByIdUseCase getCommentByIdUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final LikeCommentUseCase likeCommentUseCase;
    private final UnlikeUseCase unlikeUseCase;
    private final GetCommentLikesUseCase getCommentLikesUseCase;
    
    // Yorumların POST/GET operasyonlarını artık PostController içinde yönetiyorum
    // Endpoint: /api/v1/posts/{postId}/comments
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> getCommentById(@PathVariable Long id) {
        CommentDomain comment = getCommentByIdUseCase.execute(id);
        CommentResponse response = convertToResponse(comment);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "CommentDomain retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal UserDomain user) {
        
        CommentDomain updatedComment = updateCommentUseCase.execute(id, request.getText(), user.getId());
        CommentResponse response = convertToResponse(updatedComment);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "CommentDomain updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDomain user) {
        
        deleteCommentUseCase.execute(id, user.getId());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "CommentDomain deleted successfully", null));
    }
    
    private CommentResponse convertToResponse(CommentDomain comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        
    // Clean Architecture: sadece ID kullan, başka modüle girmiyorum
    // TODO: servis çağrısı ile user/post bilgilerini alıp doldur
    response.setPostId(comment.getPostId());
    // Şimdilik username/post title boş bırakıyorum
        
        return response;
    }
    
    // İç içe beğeni kaynağı
    @PostMapping("/{commentId}/likes")
    public ResponseEntity<ApiResponse<LikeResponse>> likeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDomain user) {
        
        LikeDomain like = likeCommentUseCase.execute(user.getId(), commentId);
        LikeResponse response = mapToLikeResponse(like);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "CommentDomain liked successfully", response));
    }
    
    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<ApiResponse<Void>> unlikeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDomain user) {
        
        unlikeUseCase.executeForComment(user.getId(), commentId);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "CommentDomain unliked successfully", null));
    }
    
    @GetMapping("/{commentId}/likes")
    public ResponseEntity<ApiResponse<List<LikeResponse>>> getCommentLikes(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<LikeDomain> likes = getCommentLikesUseCase.execute(commentId, pageable);
        
        List<LikeResponse> responses = likes.getContent().stream()
            .map(this::mapToLikeResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "CommentDomain likes retrieved successfully", responses));
    }
    
    private LikeResponse mapToLikeResponse(LikeDomain like) {
        LikeResponse response = new LikeResponse();
        response.setId(like.getId());
        response.setUserId(like.getUserId());
        
    // Clean Architecture: sadece ID kullan, modüller arası domain'e doğrudan girme
        response.setPostId(like.getPostId());
        response.setCommentId(like.getCommentId());
        
    // Şimdilik userFullName ve postTitle null bırakıyorum - servis katmanı doldurmalı
        response.setCreatedDate(like.getCreatedDate());
        return response;
    }
}
