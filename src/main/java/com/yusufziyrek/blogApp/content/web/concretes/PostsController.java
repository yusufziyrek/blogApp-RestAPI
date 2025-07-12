package com.yusufziyrek.blogApp.content.web.concretes;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.content.service.abstracts.IPostService;
import com.yusufziyrek.blogApp.content.web.abstracts.IPostsController;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class PostsController implements IPostsController {

	private final IPostService postService;

	@Override
	public ResponseEntity<ApiResponse<PageResponse<GetAllPostsResponse>>> getAll(Pageable pageable) {
		log.debug("Getting all posts with pagination: {}", pageable);
		PageResponse<GetAllPostsResponse> posts = postService.getAll(pageable);
		return ResponseEntity.ok(new ApiResponse<>(true, "Posts retrieved successfully", posts));
	}

	@Override
	public ResponseEntity<ApiResponse<PageResponse<GetAllPostsResponse>>> getAllForUser(Pageable pageable,
			@AuthenticationPrincipal User user) {
		log.debug("Getting posts for user: {} with pagination: {}", user.getUsername(), pageable);
		PageResponse<GetAllPostsResponse> posts = postService.getAllForUser(pageable, user.getId());
		return ResponseEntity.ok(new ApiResponse<>(true, "Posts retrieved successfully", posts));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdPostResponse>> getById(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id) {
		log.debug("Getting post by id: {}", id);
		GetByIdPostResponse post = postService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post retrieved successfully", post));
	}

	@Override
	public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody @Valid CreatePostRequest createPostRequest,
			@AuthenticationPrincipal User user) {
		log.info("Creating new post for user: {}", user.getUsername());
		Post createdPost = postService.createPost(createPostRequest, user);
		log.info("Post created successfully with id: {}", createdPost.getId());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Post created successfully", createdPost));
	}

	@Override
	public ResponseEntity<ApiResponse<Post>> update(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id,
			@RequestBody @Valid UpdatePostRequest updatePostRequest, @AuthenticationPrincipal User user) {
		log.info("Updating post with id: {} for user: {}", id, user.getUsername());
		Post updatedPost = postService.update(id, updatePostRequest, user);
		log.info("Post updated successfully with id: {}", updatedPost.getId());
		return ResponseEntity.ok(new ApiResponse<>(true, "Post updated successfully", updatedPost));
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id,
			@AuthenticationPrincipal User user) {
		log.info("Deleting post with id: {} for user: {}", id, user.getUsername());
		postService.delete(id, user);
		log.info("Post deleted successfully with id: {}", id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post deleted successfully", null));
	}
}
