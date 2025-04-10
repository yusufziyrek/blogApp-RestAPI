package com.yusufziyrek.blogApp.controllers.abstracts;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.entities.Like;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdLikeResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Validated
@RequestMapping("/api/v1/likes")
public interface ILikesController {

	@GetMapping("/post/{postId}")
	ResponseEntity<ApiResponse<List<GetAllLikesForPostResponse>>> getAllForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId);

	@GetMapping("/comment/{commentId}")
	ResponseEntity<ApiResponse<List<GetAllLikesForCommentResponse>>> getAllForComment(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long commentId);

	@GetMapping("/{id}")
	ResponseEntity<ApiResponse<GetByIdLikeResponse>> getById(
			@PathVariable @Positive(message = "Like ID must be a positive number") Long id);

	@PostMapping("/post/{postId}")
	ResponseEntity<ApiResponse<Like>> addLikeForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@RequestBody @Valid CreateLikeForPostRequest createLikeForPostRequest, @AuthenticationPrincipal User user);

	@PostMapping("/comment/{commentId}")
	ResponseEntity<ApiResponse<Like>> addLikeForComment(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long commentId,
			@RequestBody @Valid CreateLikeForCommentRequest createLikeForCommentRequest,
			@AuthenticationPrincipal User user);

	@DeleteMapping("/post/{likeId}")
	ResponseEntity<ApiResponse<Void>> dislikeForPost(
			@PathVariable @Positive(message = "Like ID must be a positive number") Long likeId,
			@AuthenticationPrincipal User user);

	@DeleteMapping("/comment/{likeId}")
	ResponseEntity<ApiResponse<Void>> dislikeForComment(
			@PathVariable @Positive(message = "Like ID must be a positive number") Long likeId,
			@AuthenticationPrincipal User user);
}
