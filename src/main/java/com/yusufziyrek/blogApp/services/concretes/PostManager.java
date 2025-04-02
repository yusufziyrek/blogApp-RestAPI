package com.yusufziyrek.blogApp.services.concretes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostManager implements IPostService {

	private final IPostRepository postRepository;
	private final IUserRepository userRepository;
	private final IModelMapperService modelMapperService;

	@Override
	@Cacheable(value = "allPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	public PageResponse<GetAllPostsResponse> getAll(Pageable pageable) {
		Page<Post> products = this.postRepository.findAll(pageable);
		return toPageResponse(products, GetAllPostsResponse.class);
	}

	@Override
	@Cacheable(value = "postDetails", key = "#id")
	public GetByIdPostResponse getById(Long id) {
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException("Post id not exist !"));

		return this.modelMapperService.forResponse().map(post, GetByIdPostResponse.class);
	}

	@Override
	@Cacheable(value = "userPostTitles", key = "#userId")
	public List<String> getPostForUser(Long userId) {
		return postRepository.findByUserId(userId)
			.stream()
			.map(Post::getTitle)
			.collect(Collectors.toList());
	}
	
	@Override
	@Caching(evict = {
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#createPostRequest.userId")
	})
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
	@Caching(evict = {
		@CacheEvict(value = "postDetails", key = "#id"),
		@CacheEvict(value = "allPosts", allEntries = true)
	})
	public Post update(Long id, UpdatePostRequest updatePostRequest) {
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException("Post id not exist !"));
		post.setTitle(updatePostRequest.getTitle());
		post.setText(updatePostRequest.getText());
		post.setUpdatedDate(new Date());

		return this.postRepository.save(post);
	}

	@Override
	@Caching(evict = {
		@CacheEvict(value = "postDetails", key = "#id"),
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#result") 
	})
	public Long delete(Long id) {
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException("Post id not exist !"));

		Long userId = post.getUser().getId();
		this.postRepository.deleteById(id);

		return userId; 
	}

	private <T, U> PageResponse<U> toPageResponse(Page<T> source, Class<U> targetClass) {
		List<U> items = source.getContent().stream()
				.map(item -> modelMapperService.forResponse().map(item, targetClass)).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}
}