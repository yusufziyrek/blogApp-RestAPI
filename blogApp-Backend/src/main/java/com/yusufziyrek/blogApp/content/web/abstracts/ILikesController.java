package com.yusufziyrek.blogApp.content.web.abstracts;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.content.domain.models.Like;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdLikeResponse;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;

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

	@DeleteMapping("/post/{postId}/unlike")
	ResponseEntity<ApiResponse<Void>> unlikePost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@AuthenticationPrincipal User user);
}
