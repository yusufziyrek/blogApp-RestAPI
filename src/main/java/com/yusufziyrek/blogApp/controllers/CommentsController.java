package com.yusufziyrek.blogApp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.services.abstracts.ICommentService;
import com.yusufziyrek.blogApp.services.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdCommentResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@Validated
@AllArgsConstructor
public class CommentsController {

	private ICommentService commentService;

	@GetMapping("/post/{postId}")
	public List<GetAllCommentsForPostResponse> getAllForPost(@PathVariable Long postId) {
		return this.commentService.getAllForPost(postId);

	}

	@GetMapping("/user/{userId}")
	public List<GetAllCommentsForUserResponse> getAllForUser(@PathVariable Long userId) {
		return this.commentService.getAllForUser(userId);

	}

	@GetMapping("/{id}")
	public GetByIdCommentResponse getById(@PathVariable Long id) {
		return this.commentService.getById(id);

	}

	@PostMapping("/post/{postId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Comment create(@PathVariable Long postId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
		return this.commentService.add(postId ,createCommentRequest);

	}

	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Comment update(@PathVariable Long id, @RequestBody @Valid UpdateCommentRequest updateCommentRequest) {
		return this.commentService.update(id, updateCommentRequest);

	}

	@DeleteMapping("{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		this.commentService.delete(id);

	}

}
