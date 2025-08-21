package com.yusufziyrek.blogApp.post.infrastructure.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.post.application.usecases.CreatePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetAllPostsUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostByIdUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.UpdatePostUseCase;
import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.post.dto.request.CreatePostRequest;
import com.yusufziyrek.blogApp.post.dto.request.UpdatePostRequest;
import com.yusufziyrek.blogApp.post.dto.response.PostResponse;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.ResponseMessages;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final CreatePostUseCase createPostUseCase;
    private final GetAllPostsUseCase getAllPostsUseCase;
    private final GetPostByIdUseCase getPostByIdUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts(Pageable pageable) {
        Page<Post> posts = getAllPostsUseCase.execute(pageable);
        
        List<PostResponse> responses = posts.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POSTS_RETRIEVED_SUCCESSFULLY, responses));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        Post post = getPostByIdUseCase.execute(id);
        PostResponse response = convertToResponse(post);
        
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_RETRIEVED_SUCCESSFULLY, response));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal User user) {
        
        Post createdPost = createPostUseCase.execute(request.getTitle(), request.getText(), user.getId());
        PostResponse response = convertToResponse(createdPost);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, ResponseMessages.POST_CREATED_SUCCESSFULLY, response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal User user) {
        
        Post updatedPost = updatePostUseCase.execute(id, request.getTitle(), request.getText(), user.getId());
        PostResponse response = convertToResponse(updatedPost);
        
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_UPDATED_SUCCESSFULLY, response));
    }
    
    private PostResponse convertToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setText(post.getText());
        response.setCreatedDate(post.getCreatedDate());
        response.setUpdatedDate(post.getUpdatedDate());
        response.setCommentCount(post.getCommentCount());
        response.setLikeCount(post.getLikeCount());
        
        if (post.getUser() != null) {
            response.setAuthorUsername(post.getUser().getUsername());
            response.setAuthorFullName(post.getUser().getFullName());
        }
        
        return response;
    }
}
