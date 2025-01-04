package com.yusufziyrek.blogApp.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.services.abstracts.IPostService;
import com.yusufziyrek.blogApp.services.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.services.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@Validated
@AllArgsConstructor
public class PostsController {

	private IPostService postService;

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<GetAllPostsResponse>>> getAll(Pageable pageable) {
		PageResponse<GetAllPostsResponse> posts = postService.getAll(pageable);
		return ResponseEntity.ok(new ApiResponse<>(true, "Posts retrieved successfully", posts));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<GetByIdPostResponse>> getById(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id) {
		GetByIdPostResponse post = postService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post retrieved successfully", post));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody @Valid CreatePostRequest createPostRequest) {
		Post createdPost = postService.createPost(createPostRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Post created successfully", createdPost));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Post>> update(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id,
			@RequestBody @Valid UpdatePostRequest updatePostRequest) {
		Post updatedPost = postService.update(id, updatePostRequest);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post updated successfully", updatedPost));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id) {
		postService.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Post deleted successfully", null));
	}
}