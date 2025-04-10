package com.yusufziyrek.blogApp.controllers.abstracts;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdCommentResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Validated
@RequestMapping("/api/v1/comments")
public interface ICommentsController {

	@GetMapping("/post/{postId}")
	ResponseEntity<ApiResponse<List<GetAllCommentsForPostResponse>>> getAllForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId);

	@GetMapping("/user")
	ResponseEntity<ApiResponse<List<GetAllCommentsForUserResponse>>> getAllForUser(@AuthenticationPrincipal User user);

	@GetMapping("/{id}")
	ResponseEntity<ApiResponse<GetByIdCommentResponse>> getById(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id);

	@PostMapping("/post/{postId}")
	ResponseEntity<ApiResponse<Comment>> create(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@RequestBody @Valid CreateCommentRequest createCommentRequest, @AuthenticationPrincipal User user);

	@PutMapping("/{id}")
	ResponseEntity<ApiResponse<Comment>> update(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id,
			@RequestBody @Valid UpdateCommentRequest updateCommentRequest, @AuthenticationPrincipal User user);

	@DeleteMapping("/{id}")
	ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id,
			@AuthenticationPrincipal User user);
}
