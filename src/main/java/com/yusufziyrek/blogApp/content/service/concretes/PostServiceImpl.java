package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.domain.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.IPostService;
import com.yusufziyrek.blogApp.identity.domain.User;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.mapper.IModelMapperService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {

	private final IPostRepository postRepository;
	private final IModelMapperService modelMapperService;

	@Override
	@Cacheable(value = "allPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	public PageResponse<GetAllPostsResponse> getAll(Pageable pageable) {
		Page<Post> posts = this.postRepository.findAll(pageable);
		return toPageResponse(posts, GetAllPostsResponse.class);
	}

	@Override
	@Cacheable(value = "allPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #userId")
	public PageResponse<GetAllPostsResponse> getAllForUser(Pageable pageable, Long userId) {
		Page<Post> posts = this.postRepository.findAllByUserId(pageable, userId);
		return toPageResponse(posts, GetAllPostsResponse.class);
	}

	@Override
	@Cacheable(value = "postDetails", key = "#id")
	public GetByIdPostResponse getById(Long id) {
		Post post = this.postRepository.findById(id).orElseThrow(() -> new PostException("Post id not exist !"));
		GetByIdPostResponse response = this.modelMapperService.forResponse().map(post, GetByIdPostResponse.class);
		response.setAuthorUser(post.getUser().getUsername());
		return response;
	}

	@Override
	@Cacheable(value = "userPostTitles", key = "#userId")
	public List<String> getPostTitleForUser(Long userId) {
		return postRepository.findByUserId(userId).stream().map(Post::getTitle).collect(Collectors.toList());
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "allPosts", allEntries = true),
			@CacheEvict(value = "userPostTitles", key = "#user.id") })
	public Post createPost(CreatePostRequest createPostRequest, User user) {
		Post post = new Post();
		post.setUser(user);
		post.setTitle(createPostRequest.getTitle());
		post.setText(createPostRequest.getText());
		post.setCreatedDate(new Date());
		post.setUpdatedDate(new Date());

		return this.postRepository.save(post);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "postDetails", key = "#id"),
			@CacheEvict(value = "allPosts", allEntries = true) })
	public Post update(Long id, UpdatePostRequest updatePostRequest, User user) {
		Post post = this.postRepository.findById(id).orElseThrow(() -> new PostException("Post id not exist !"));
		if (!post.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to update this post.");
		}
		post.setTitle(updatePostRequest.getTitle());
		post.setText(updatePostRequest.getText());
		post.setUpdatedDate(new Date());

		return this.postRepository.save(post);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "postDetails", key = "#id"),
			@CacheEvict(value = "allPosts", allEntries = true),
			@CacheEvict(value = "userPostTitles", key = "#user.id") })
	public Long delete(Long id, User user) {
		Post post = this.postRepository.findById(id).orElseThrow(() -> new PostException("Post id not exist !"));
		if (!post.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to delete this post.");
		}
		Long userId = post.getUser().getId();
		this.postRepository.deleteById(id);
		return userId;
	}

	private <T, U> PageResponse<U> toPageResponse(Page<T> source, Class<U> targetClass) {
		List<U> items = source.getContent().stream().map(item -> {
			U response = modelMapperService.forResponse().map(item, targetClass);
			if (item instanceof Post && response instanceof GetAllPostsResponse) {
				GetAllPostsResponse postResponse = (GetAllPostsResponse) response;
				Post post = (Post) item;
				postResponse.setAuthorUser(post.getUser().getUsername());
			}
			return response;
		}).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}
}
