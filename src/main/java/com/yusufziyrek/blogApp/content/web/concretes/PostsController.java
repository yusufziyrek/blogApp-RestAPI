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

@RestController
@Validated
@RequiredArgsConstructor
public class PostsController implements IPostsController {

	private final IPostService postService;

	@Override
	public ResponseEntity<ApiResponse<PageResponse<GetAllPostsResponse>>> getAll(Pageable pageable) {
		PageResponse<GetAllPostsResponse> posts = postService.getAll(pageable);
		return ResponseEntity.ok(new ApiResponse<>(true, "Posts retrieved successfully", posts));
	}

	@Override
	public ResponseEntity<ApiResponse<PageResponse<GetAllPostsResponse>>> getAllForUser(Pageable pageable,
			@AuthenticationPrincipal User user) {
		PageResponse<GetAllPostsResponse> posts = postService.getAllForUser(pageable, user.getId());
		return ResponseEntity.ok(new ApiResponse<>(true, "Posts retrieved successfully", posts));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdPostResponse>> getById(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id) {
		GetByIdPostResponse post = postService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post retrieved successfully", post));
	}

	@Override
	public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody @Valid CreatePostRequest createPostRequest,
			@AuthenticationPrincipal User user) {
		Post createdPost = postService.createPost(createPostRequest, user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Post created successfully", createdPost));
	}

	@Override
	public ResponseEntity<ApiResponse<Post>> update(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id,
			@RequestBody @Valid UpdatePostRequest updatePostRequest, @AuthenticationPrincipal User user) {
		Post updatedPost = postService.update(id, updatePostRequest, user);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post updated successfully", updatedPost));
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id,
			@AuthenticationPrincipal User user) {
		postService.delete(id, user);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post deleted successfully", null));
	}
}
