package com.yusufziyrek.blogApp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.entities.Like;
import com.yusufziyrek.blogApp.services.abstracts.ILikeService;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdLikeResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/likes")
@Validated
@AllArgsConstructor
public class LikesController {

	private ILikeService likeService;

	@GetMapping("/post/{postId}")
	public List<GetAllLikesForPostResponse> getAllForPost(@PathVariable Long postId) {
		return this.likeService.getAllForPost(postId);

	}

	@GetMapping("/comment/{commentId}")
	public List<GetAllLikesForCommentResponse> getAllForComment(@PathVariable Long commentId) {
		return this.likeService.getAllLikesForComment(commentId);

	}

	@GetMapping("/{id}")
	public GetByIdLikeResponse getById(@PathVariable Long id) {
		return this.likeService.getById(id);

	}

	@PostMapping("/post/{postId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Like addLikeForPost(@PathVariable Long postId,
			@RequestBody @Valid CreateLikeForPostRequest createLikeForPostRequest) {
		return this.likeService.addLikeForPost(postId, createLikeForPostRequest);

	}

	@PostMapping("/comment/{commentId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Like addLikeForComment(@PathVariable Long commentId,
			@RequestBody @Valid CreateLikeForCommentRequest createLikeForCommentRequest) {
		return this.likeService.addLikeForComment(commentId, createLikeForCommentRequest);

	}

	@DeleteMapping("/post/{postId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void dislikeForPost(@PathVariable Long id) {
		this.likeService.dislikeForPost(id);

	}

	@DeleteMapping("/comment/{id}")
	public void dislikeForComment(@PathVariable Long id) {
		this.likeService.dislikeForComment(id);

	}

}
