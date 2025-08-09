package com.yusufziyrek.blogApp.content.web.concretes;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.content.domain.models.Like;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdLikeResponse;
import com.yusufziyrek.blogApp.content.service.abstracts.ILikeService;
import com.yusufziyrek.blogApp.content.web.abstracts.ILikesController;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.ResponseMessages;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
public class LikesController implements ILikesController {

	private final ILikeService likeService;

	@Override
	public ResponseEntity<ApiResponse<List<GetAllLikesForPostResponse>>> getAllForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId) {
		List<GetAllLikesForPostResponse> likes = likeService.getAllForPost(postId);
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.LIKES_FOR_POST_RETRIEVED_SUCCESSFULLY, likes));
	}

	@Override
	public ResponseEntity<ApiResponse<List<GetAllLikesForCommentResponse>>> getAllForComment(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long commentId) {
		List<GetAllLikesForCommentResponse> likes = likeService.getAllLikesForComment(commentId);
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.LIKES_FOR_COMMENT_RETRIEVED_SUCCESSFULLY, likes));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdLikeResponse>> getById(
			@PathVariable @Positive(message = "Like ID must be a positive number") Long id) {
		GetByIdLikeResponse like = likeService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.LIKE_RETRIEVED_SUCCESSFULLY, like));
	}

	@Override
	public ResponseEntity<ApiResponse<Like>> addLikeForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@RequestBody @Valid CreateLikeForPostRequest createLikeForPostRequest, @AuthenticationPrincipal User user) {
		Like like = likeService.addLikeForPost(postId, createLikeForPostRequest, user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, ResponseMessages.LIKE_FOR_POST_ADDED_SUCCESSFULLY, like));
	}

	@Override
	public ResponseEntity<ApiResponse<Like>> addLikeForComment(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long commentId,
			@RequestBody @Valid CreateLikeForCommentRequest createLikeForCommentRequest,
			@AuthenticationPrincipal User user) {
		Like like = likeService.addLikeForComment(commentId, createLikeForCommentRequest, user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, ResponseMessages.LIKE_FOR_COMMENT_ADDED_SUCCESSFULLY, like));
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> unlikePost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@AuthenticationPrincipal User user) {
		likeService.unlikePost(postId, user);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(new ApiResponse<>(true, ResponseMessages.LIKE_REMOVED_FROM_POST_SUCCESSFULLY, null));
	}
}
