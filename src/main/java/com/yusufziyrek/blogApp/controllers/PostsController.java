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

import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.services.abstracts.IPostService;
import com.yusufziyrek.blogApp.services.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.services.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdPostResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@Validated
@AllArgsConstructor
public class PostsController {

	private IPostService postService;

	@GetMapping
	public List<GetAllPostsResponse> getAll() {
		return this.postService.getAll();

	}

	@GetMapping("/{id}")
	public GetByIdPostResponse getById(@PathVariable Long id) {
		return this.postService.getById(id);

	}

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public Post createPost(@RequestBody @Valid CreatePostRequest createPostRequest) {
		return this.postService.createPost(createPostRequest);
	}

	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Post update(@PathVariable Long id, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
		return this.postService.update(id, updatePostRequest);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		this.postService.delete(id);
	}

}
