package com.yusufziyrek.blogApp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.entities.Like;
import com.yusufziyrek.blogApp.services.abstracts.ILikeService;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdLikeResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/likes")
@Validated
@RequiredArgsConstructor
public class LikesController {

	private final ILikeService likeService;

	@GetMapping("/post/{postId}")
	public ResponseEntity<ApiResponse<List<GetAllLikesForPostResponse>>> getAllForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId) {
		List<GetAllLikesForPostResponse> likes = likeService.getAllForPost(postId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Likes for post retrieved successfully", likes));
	}

	@GetMapping("/comment/{commentId}")
	public ResponseEntity<ApiResponse<List<GetAllLikesForCommentResponse>>> getAllForComment(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long commentId) {
		List<GetAllLikesForCommentResponse> likes = likeService.getAllLikesForComment(commentId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Likes for comment retrieved successfully", likes));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<GetByIdLikeResponse>> getById(
			@PathVariable @Positive(message = "Like ID must be a positive number") Long id) {
		GetByIdLikeResponse like = likeService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Like retrieved successfully", like));
	}

	@PostMapping("/post/{postId}")
	public ResponseEntity<ApiResponse<Like>> addLikeForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@RequestBody @Valid CreateLikeForPostRequest createLikeForPostRequest) {
		Like like = likeService.addLikeForPost(postId, createLikeForPostRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Like for post added successfully", like));
	}

	@PostMapping("/comment/{commentId}")
	public ResponseEntity<ApiResponse<Like>> addLikeForComment(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long commentId,
			@RequestBody @Valid CreateLikeForCommentRequest createLikeForCommentRequest) {
		Like like = likeService.addLikeForComment(commentId, createLikeForCommentRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Like for comment added successfully", like));
	}

	@DeleteMapping("/post/{postId}")
	public ResponseEntity<ApiResponse<Void>> dislikeForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId) {
		likeService.dislikeForPost(postId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Disliked post successfully", null));
	}

	@DeleteMapping("/comment/{id}")
	public ResponseEntity<ApiResponse<Void>> dislikeForComment(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id) {
		likeService.dislikeForComment(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Disliked comment successfully", null));
	}
}