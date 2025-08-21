package com.yusufziyrek.blogApp.comment.infrastructure.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.comment.application.usecases.CreateCommentUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentsForPostUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentByIdUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.UpdateCommentUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.DeleteCommentUseCase;
import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.comment.dto.request.CreateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.request.UpdateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CreateCommentUseCase createCommentUseCase;
    private final GetCommentsForPostUseCase getCommentsForPostUseCase;
    private final GetCommentByIdUseCase getCommentByIdUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    
    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsForPost(
            @PathVariable Long postId,
            Pageable pageable) {
        
        Page<Comment> comments = getCommentsForPostUseCase.execute(postId, pageable);
        
        List<CommentResponse> responses = comments.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Comments retrieved successfully", responses));
    }
    
    @PostMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal User user) {
        
        Comment createdComment = createCommentUseCase.execute(request.getText(), postId, user.getId());
        CommentResponse response = convertToResponse(createdComment);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Comment created successfully", response));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> getCommentById(@PathVariable Long id) {
        Comment comment = getCommentByIdUseCase.execute(id);
        CommentResponse response = convertToResponse(comment);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal User user) {
        
        Comment updatedComment = updateCommentUseCase.execute(id, request.getText(), user.getId());
        CommentResponse response = convertToResponse(updatedComment);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        
        deleteCommentUseCase.execute(id, user.getId());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment deleted successfully", null));
    }
    
    private CommentResponse convertToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        
        if (comment.getUser() != null) {
            response.setAuthorUsername(comment.getUser().getUsername());
            response.setAuthorFullName(comment.getUser().getFullName());
        }
        
        if (comment.getPost() != null) {
            response.setPostId(comment.getPost().getId());
            response.setPostTitle(comment.getPost().getTitle());
        }
        
        return response;
    }
}
