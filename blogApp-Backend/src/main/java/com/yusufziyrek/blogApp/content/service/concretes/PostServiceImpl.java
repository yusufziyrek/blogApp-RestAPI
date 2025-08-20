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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements IPostService {

	private final IPostRepository postRepository;
	private final PostMapper postMapper;

	@Override
	@Cacheable(value = "allPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
	public PageResponse<GetAllPostsResponse> getAll(Pageable pageable) {
		log.info("Getting all posts - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Post> posts = this.postRepository.findAll(pageable);
		log.info("Found {} posts", posts.getTotalElements());
		return toPageResponse(posts);
	}

	@Override
	@Cacheable(value = "userPosts", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #userId")
	public PageResponse<GetAllPostsResponse> getAllForUser(Pageable pageable, Long userId) {
		log.info("Getting posts for user ID: {} - page: {}, size: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
		Page<Post> posts = this.postRepository.findAllByUserId(pageable, userId);
		log.info("Found {} posts for user ID: {}", posts.getTotalElements(), userId);
		return toPageResponse(posts);
	}

	@Override
	@Cacheable(value = "postDetails", key = "#id")
	public GetByIdPostResponse getById(Long id) {
		log.info("Getting post by ID: {}", id);
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, id)));
		log.info("Successfully found post: {}", post.getTitle());
		return this.postMapper.toGetByIdResponse(post);
	}

	@Override
	@Cacheable(value = "userPostTitles", key = "#userId")
	public List<String> getPostTitleForUser(Long userId) {
		log.info("Getting post titles for user ID: {}", userId);
		List<String> titles = postRepository.findByUserId(userId).stream().map(Post::getTitle).collect(Collectors.toList());
		log.info("Found {} post titles for user ID: {}", titles.size(), userId);
		return titles;
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#user.id") 
	})
	public Post createPost(CreatePostRequest createPostRequest, User user) {
		log.info("Creating new post for user: {} with title: {}", user.getUsername(), createPostRequest.getTitle());
		Post post = this.postMapper.toPost(createPostRequest);
		post.setUser(user);
		Post savedPost = this.postRepository.save(post);
		log.info("Successfully created post with ID: {}", savedPost.getId());
		return savedPost;
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "postDetails", key = "#id"),
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#user.id") 
	})
	public Post update(Long id, UpdatePostRequest updatePostRequest, User user) {
		log.info("Updating post with ID: {} by user: {}", id, user.getUsername());
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, id)));
		if (!post.getUser().getId().equals(user.getId())) {
			log.warn("Access denied: User {} tried to update post {} owned by another user", user.getUsername(), id);
			throw new AccessDeniedException(ErrorMessages.POST_ACCESS_DENIED_UPDATE);
		}
		this.postMapper.updatePostFromRequest(post, updatePostRequest);
		Post updatedPost = this.postRepository.save(post);
		log.info("Successfully updated post with ID: {}", id);
		return updatedPost;
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "postDetails", key = "#id"),
		@CacheEvict(value = "allPosts", allEntries = true),
		@CacheEvict(value = "userPosts", allEntries = true),
		@CacheEvict(value = "userPostTitles", key = "#user.id") 
	})
	public Long delete(Long id, User user) {
		log.info("Deleting post with ID: {} by user: {}", id, user.getUsername());
		Post post = this.postRepository.findById(id)
				.orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, id)));
		if (!post.getUser().getId().equals(user.getId())) {
			log.warn("Access denied: User {} tried to delete post {} owned by another user", user.getUsername(), id);
			throw new AccessDeniedException(ErrorMessages.POST_ACCESS_DENIED_DELETE);
		}
		Long userId = post.getUser().getId();
		this.postRepository.deleteById(id);
		log.info("Successfully deleted post with ID: {}", id);
		return userId;
	}

	private PageResponse<GetAllPostsResponse> toPageResponse(Page<Post> source) {
		List<GetAllPostsResponse> items = source.getContent().stream()
				.map(postMapper::toGetAllPostsResponse).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}
}
