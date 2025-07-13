package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.content.mapper.PostMapper;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.IPostService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {

	private final IPostRepository postRepository;
	private final PostMapper postMapper;

	@Override
	@Cacheable(value = "allPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
	public PageResponse<GetAllPostsResponse> getAll(Pageable pageable) {
		Page<Post> posts = this.postRepository.findAll(pageable);
		return toPageResponse(posts);
	}

	@Override
	@Cacheable(value = "userPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #userId")
	public PageResponse<GetAllPostsResponse> getAllForUser(Pageable pageable, Long userId) {
		Page<Post> posts = this.postRepository.findAllByUserId(pageable, userId);
		return toPageResponse(posts);
	}

	@Override
	@Cacheable(value = "postDetails", key = "#id")
	public GetByIdPostResponse getById(Long id) {
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, id)));
		return this.postMapper.toGetByIdResponse(post);
	}

	@Override
	@Cacheable(value = "userPostTitles", key = "#userId")
	public List<String> getPostTitleForUser(Long userId) {
		return postRepository.findByUserId(userId).stream().map(Post::getTitle).collect(Collectors.toList());
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#user.id") 
	})
	public Post createPost(CreatePostRequest createPostRequest, User user) {
		Post post = this.postMapper.toPost(createPostRequest);
		post.setUser(user);
		return this.postRepository.save(post);
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "postDetails", key = "#id"),
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#user.id") 
	})
	public Post update(Long id, UpdatePostRequest updatePostRequest, User user) {
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, id)));
		if (!post.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException(ErrorMessages.POST_ACCESS_DENIED_UPDATE);
		}
		this.postMapper.updatePostFromRequest(post, updatePostRequest);
		return this.postRepository.save(post);
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "postDetails", key = "#id"),
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#user.id") 
	})
	public Long delete(Long id, User user) {
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, id)));
		if (!post.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException(ErrorMessages.POST_ACCESS_DENIED_DELETE);
		}
		Long userId = post.getUser().getId();
		this.postRepository.deleteById(id);
		return userId;
	}

	private PageResponse<GetAllPostsResponse> toPageResponse(Page<Post> source) {
		List<GetAllPostsResponse> items = source.getContent().stream()
				.map(postMapper::toGetAllPostsResponse).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}
}
