package com.yusufziyrek.blogApp.controllers.concretes;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.controllers.abstracts.ICommentsController;
import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.abstracts.ICommentService;
import com.yusufziyrek.blogApp.services.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdCommentResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
public class CommentsController implements ICommentsController {

	private final ICommentService commentService;

	@Override
	public ResponseEntity<ApiResponse<List<GetAllCommentsForPostResponse>>> getAllForPost(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId) {
		List<GetAllCommentsForPostResponse> comments = commentService.getAllForPost(postId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comments for post retrieved successfully", comments));
	}

	@Override
	public ResponseEntity<ApiResponse<List<GetAllCommentsForUserResponse>>> getAllForUser(
			@AuthenticationPrincipal User user) {
		List<GetAllCommentsForUserResponse> comments = commentService.getAllForUser(user.getId());
		return ResponseEntity.ok(new ApiResponse<>(true, "Comments for user retrieved successfully", comments));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdCommentResponse>> getById(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id) {
		GetByIdCommentResponse comment = commentService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comment retrieved successfully", comment));
	}

	@Override
	public ResponseEntity<ApiResponse<Comment>> create(
			@PathVariable @Positive(message = "Post ID must be a positive number") Long postId,
			@RequestBody @Valid CreateCommentRequest createCommentRequest, @AuthenticationPrincipal User user) {
		Comment createdComment = commentService.add(postId, createCommentRequest, user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Comment added successfully", createdComment));
	}

	@Override
	public ResponseEntity<ApiResponse<Comment>> update(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id,
			@RequestBody @Valid UpdateCommentRequest updateCommentRequest, @AuthenticationPrincipal User user) {
		Comment updatedComment = commentService.update(id, updateCommentRequest, user);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comment updated successfully", updatedComment));
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "Comment ID must be a positive number") Long id,
			@AuthenticationPrincipal User user) {
		commentService.delete(id, user);
		return ResponseEntity.ok(new ApiResponse<>(true, "Comment deleted successfully", null));
	}
}
