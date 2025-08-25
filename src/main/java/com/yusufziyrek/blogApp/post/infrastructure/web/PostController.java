package com.yusufziyrek.blogApp.post.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.post.application.services.PostApplicationService;
import com.yusufziyrek.blogApp.post.dto.request.CreatePostRequest;
import com.yusufziyrek.blogApp.post.dto.request.UpdatePostRequest;
import com.yusufziyrek.blogApp.post.dto.response.PostResponse;
import com.yusufziyrek.blogApp.comment.dto.request.CreateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.like.dto.response.LikeResponse;
import com.yusufziyrek.blogApp.shared.security.UserPrincipal;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.ResponseMessages;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final PostApplicationService postApplicationService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<PostResponse> responses = postApplicationService.getAllPosts(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POSTS_RETRIEVED_SUCCESSFULLY, responses));
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getCurrentUserPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal user) {
        
        List<PostResponse> responses = postApplicationService.getCurrentUserPosts(
                user.getId(), user.getUsername(), page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Your posts retrieved successfully", responses));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        PostResponse response = postApplicationService.getPostById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_RETRIEVED_SUCCESSFULLY, response));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        PostResponse response = postApplicationService.createPost(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, ResponseMessages.POST_CREATED_SUCCESSFULLY, response));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        PostResponse response = postApplicationService.updatePost(id, request, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_UPDATED_SUCCESSFULLY, response));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        
        postApplicationService.deletePost(id, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_DELETED_SUCCESSFULLY, null));
    }
    
    // Cross-module operations coordinated through Application Service
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getPostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<CommentResponse> responses = postApplicationService.getCommentsForPost(postId, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comments retrieved successfully", responses));
    }
    
    @PostMapping("/{postId}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CommentResponse>> createPostComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        CommentResponse response = postApplicationService.createCommentForPost(postId, request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Comment created successfully", response));
    }
    
    @PostMapping("/{postId}/likes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<LikeResponse>> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal user) {
        
        LikeResponse response = postApplicationService.likePost(postId, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Post liked successfully", response));
    }
    
    @DeleteMapping("/{postId}/likes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal user) {
        
        postApplicationService.unlikePost(postId, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Post unliked successfully", null));
    }
    
    @GetMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<List<LikeResponse>>> getPostLikes(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<LikeResponse> responses = postApplicationService.getPostLikes(postId, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post likes retrieved successfully", responses));
    }
}
