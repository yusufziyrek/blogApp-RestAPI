package com.yusufziyrek.blogApp.post.infrastructure.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.post.application.usecases.CreatePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.DeletePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetAllPostsUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostByIdUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostsByUserIdUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.UpdatePostUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.CreateCommentUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentsForPostUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.LikePostUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.UnlikeUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.GetPostLikesUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByIdUseCase;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.dto.request.CreatePostRequest;
import com.yusufziyrek.blogApp.post.dto.request.UpdatePostRequest;
import com.yusufziyrek.blogApp.post.dto.response.PostResponse;
import com.yusufziyrek.blogApp.comment.dto.request.CreateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.dto.response.LikeResponse;
import com.yusufziyrek.blogApp.post.infrastructure.mappers.PostMapper;
import com.yusufziyrek.blogApp.shared.security.UserPrincipal;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.dto.ResponseMessages;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final CreatePostUseCase createPostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final GetAllPostsUseCase getAllPostsUseCase;
    private final GetPostByIdUseCase getPostByIdUseCase;
    private final GetPostsByUserIdUseCase getPostsByUserIdUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final CreateCommentUseCase createCommentUseCase;
    private final GetCommentsForPostUseCase getCommentsForPostUseCase;
    private final LikePostUseCase likePostUseCase;
    private final UnlikeUseCase unlikeUseCase;
    private final GetPostLikesUseCase getPostLikesUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final PostMapper postMapper;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        com.yusufziyrek.blogApp.shared.dto.PageResponse<PostDomain> pageResponse = 
            getAllPostsUseCase.execute(page, size);
        
        List<PostResponse> responses = pageResponse.getItems().stream()
                .map(postDomain -> {
                    // Her post için gerçek kullanıcı bilgisini çekiyorum
                    UserResponse user = getUserByIdUseCase.execute(postDomain.getUserId());
                    String fullName = user.getFirstname() + " " + user.getLastname();
                    return postMapper.toResponse(postDomain, user.getUsername(), fullName);
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POSTS_RETRIEVED_SUCCESSFULLY, responses));
    }    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getCurrentUserPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal user) {
        
        com.yusufziyrek.blogApp.shared.dto.PageResponse<PostDomain> pageResponse = 
            getPostsByUserIdUseCase.execute(user.getId(), page, size);
        
        List<PostResponse> responses = pageResponse.getItems().stream()
                .map(postDomain -> {
                    String fullName = user.getUsername(); // Kendi gönderilerimde username'i kullanıyorum
                    return postMapper.toResponse(postDomain, user.getUsername(), fullName);
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Your posts retrieved successfully", responses));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        PostDomain post = getPostByIdUseCase.execute(id);
    // Bu post için kullanıcı bilgisini çekiyorum
        UserResponse user = getUserByIdUseCase.execute(post.getUserId());
        String fullName = user.getFirstname() + " " + user.getLastname();
        PostResponse response = postMapper.toResponse(post, user.getUsername(), fullName);
        
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_RETRIEVED_SUCCESSFULLY, response));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        PostDomain createdPost = createPostUseCase.execute(request, user.getId());
        PostResponse response = postMapper.toResponse(createdPost, user.getUsername(), user.getUsername());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, ResponseMessages.POST_CREATED_SUCCESSFULLY, response));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        PostDomain updatedPost = updatePostUseCase.execute(id, request.getTitle(), request.getText(), user.getId());
        PostResponse response = postMapper.toResponse(updatedPost, user.getUsername(), user.getUsername());
        
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_UPDATED_SUCCESSFULLY, response));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        
        deletePostUseCase.execute(id, user.getId());
        
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.POST_DELETED_SUCCESSFULLY, null));
    }
    
    // İç içe yorum kaynağı
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getPostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageResponse<CommentDomain> pageResponse = 
            getCommentsForPostUseCase.execute(postId, page, size);
        
        List<CommentResponse> responses = pageResponse.getItems().stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Comments retrieved successfully", responses));
    }
    
    @PostMapping("/{postId}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CommentResponse>> createPostComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        CommentResponse response = createCommentUseCase.execute(request, user.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "CommentDomain created successfully", response));
    }
    
    // İç içe beğeni kaynağı
    @PostMapping("/{postId}/likes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<LikeResponse>> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal user) {
        
        LikeDomain like = likePostUseCase.execute(user.getId(), postId);
        LikeResponse response = mapToLikeResponse(like);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "PostDomain liked successfully", response));
    }
    
    @DeleteMapping("/{postId}/likes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal user) {
        
        unlikeUseCase.executeForPost(user.getId(), postId);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "PostDomain unliked successfully", null));
    }
    
    @GetMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<List<LikeResponse>>> getPostLikes(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<LikeDomain> likes = getPostLikesUseCase.execute(postId, pageable);
        
        List<LikeResponse> responses = likes.getContent().stream()
            .map(this::mapToLikeResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "PostDomain likes retrieved successfully", responses));
    }
    
    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<ApiResponse<Long>> getPostLikeCount(@PathVariable Long postId) {
        long count = getPostLikesUseCase.getPostLikeCount(postId);
        return ResponseEntity.ok(new ApiResponse<>(true, "PostDomain like count retrieved successfully", count));
    }
    
    private LikeResponse mapToLikeResponse(LikeDomain like) {
        LikeResponse response = new LikeResponse();
        response.setId(like.getId());
        response.setUserId(like.getUserId());
        
    // Clean Architecture: sadece ID tut, başka modüle girme
        response.setPostId(like.getPostId());
        response.setCommentId(like.getCommentId());
        
    // Şimdilik userFullName/postTitle null bırakıyorum; servis katmanı dolduracak
        response.setCreatedDate(like.getCreatedDate());
        return response;
    }
    
    private CommentResponse convertToCommentResponse(CommentDomain comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        
    // Clean Architecture: sadece ID kullan, modüller arası erişme
        response.setPostId(comment.getPostId());
    // Şimdilik authorUsername/postTitle boş; servis doldurmalı
        
        return response;
    }
}
