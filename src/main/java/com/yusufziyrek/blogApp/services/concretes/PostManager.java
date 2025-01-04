package com.yusufziyrek.blogApp.services.concretes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.repos.IPostRepository;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.services.abstracts.IPostService;
import com.yusufziyrek.blogApp.services.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.services.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;
import com.yusufziyrek.blogApp.utilites.exceptions.PostException;
import com.yusufziyrek.blogApp.utilites.mappers.IModelMapperService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostManager implements IPostService {

	private IPostRepository postRepository;
	private IUserRepository userRepository;
	private IModelMapperService modelMapperService;

	@Override
	public PageResponse<GetAllPostsResponse> getAll(Pageable pageable) {

		Page<Post> products = this.postRepository.findAll(pageable);
		return toPageResponse(products, GetAllPostsResponse.class);
	}

	@Override
	public GetByIdPostResponse getById(Long id) {

		Post post = this.postRepository.findById(id).orElseThrow(() -> new PostException("Post id not exist !"));

		GetByIdPostResponse response = this.modelMapperService.forResponse().map(post, GetByIdPostResponse.class);

		return response;
	}

	@Override
	public List<String> getPostForUser(Long userId) {
		List<Post> userPosts = postRepository.findByUserId(userId);
		List<String> postTitles = new ArrayList<>();
		for (Post post : userPosts) {
			postTitles.add(post.getTitle());
		}
		return postTitles;
	}

	@Override
	public Post createPost(CreatePostRequest createPostRequest) {

		Post post = new Post();
		post.setUser(this.userRepository.findById(createPostRequest.getUserId())
				.orElseThrow(() -> new PostException("User id not exist !")));
		post.setTitle(createPostRequest.getTitle());
		post.setText(createPostRequest.getText());
		post.setCreatedDate(new Date());
		post.setUpdatedDate(new Date());

		return this.postRepository.save(post);

	}

	@Override
	public Post update(Long id, UpdatePostRequest updatePostRequest) {

		Post post = this.postRepository.findById(id).orElseThrow(() -> new PostException("Post id not exist !"));
		post.setTitle(updatePostRequest.getTitle());
		post.setText(updatePostRequest.getText());
		post.setUpdatedDate(new Date());

		return this.postRepository.save(post);
	}

	@Override
	public void delete(Long id) {

		this.postRepository.deleteById(id);

	}

	private <T, U> PageResponse<U> toPageResponse(Page<T> source, Class<U> targetClass) {
		List<U> items = source.getContent().stream()
				.map(item -> modelMapperService.forResponse().map(item, targetClass)).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}

}
