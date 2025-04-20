package com.yusufziyrek.blogApp.content.web.abstracts;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.content.domain.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.identity.domain.User;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Validated
@RequestMapping("/api/v1/posts")
public interface IPostsController {

	@GetMapping
	ResponseEntity<ApiResponse<PageResponse<GetAllPostsResponse>>> getAll(Pageable pageable);

	@GetMapping("/me")
	ResponseEntity<ApiResponse<PageResponse<GetAllPostsResponse>>> getAllForUser(Pageable pageable,
			@AuthenticationPrincipal User user);

	@GetMapping("/{id}")
	ResponseEntity<ApiResponse<GetByIdPostResponse>> getById(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id);

	@PostMapping
	ResponseEntity<ApiResponse<Post>> createPost(@RequestBody @Valid CreatePostRequest createPostRequest,
			@AuthenticationPrincipal User user);

	@PutMapping("/{id}")
	ResponseEntity<ApiResponse<Post>> update(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id,
			@RequestBody @Valid UpdatePostRequest updatePostRequest, @AuthenticationPrincipal User user);

	@DeleteMapping("/{id}")
	ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long id,
			@AuthenticationPrincipal User user);
}
