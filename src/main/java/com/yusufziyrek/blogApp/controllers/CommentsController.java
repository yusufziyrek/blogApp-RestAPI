package com.yusufziyrek.blogApp.controllers;

import java.util.List;

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

import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.services.abstracts.ICommentService;
import com.yusufziyrek.blogApp.services.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdCommentResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@Validated
@AllArgsConstructor
public class CommentsController {

	private ICommentService commentService;

	@GetMapping("/post/{postId}")
	public ResponseEntity<ApiResponse<List<GetAllCommentsForPostResponse>>> getAllForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId) {
		List<GetAllCommentsForPostResponse> comments = commentService.getAllForPost(postId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comments for post retrieved successfully", comments));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<GetAllCommentsForUserResponse>>> getAllForUser(
			@PathVariable @Positive(message = "User ID must be a positive number") Long userId) {
		List<GetAllCommentsForUserResponse> comments = commentService.getAllForUser(userId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comments for user retrieved successfully", comments));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<GetByIdCommentResponse>> getById(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id) {
		GetByIdCommentResponse comment = commentService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comment retrieved successfully", comment));
	}

	@PostMapping("/post/{postId}")
	public ResponseEntity<ApiResponse<Comment>> create(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@RequestBody @Valid CreateCommentRequest createCommentRequest) {
		Comment createdComment = commentService.add(postId, createCommentRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Comment added successfully", createdComment));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Comment>> update(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id,
			@RequestBody @Valid UpdateCommentRequest updateCommentRequest) {
		Comment updatedComment = commentService.update(id, updateCommentRequest);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comment updated successfully", updatedComment));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id) {
		commentService.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comment deleted successfully", null));
	}
}
