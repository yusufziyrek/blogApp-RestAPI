package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.domain.models.Comment;
import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdCommentResponse;
import com.yusufziyrek.blogApp.content.repo.ICommentRepository;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ICommentService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.mapper.IModelMapperService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {

	private final ICommentRepository commentRepository;
	private final IPostRepository postRepository;
	private final IModelMapperService modelMapperService;

	@Override
	@Cacheable(value = "commentsForPost", key = "#postId")
	public List<GetAllCommentsForPostResponse> getAllForPost(Long postId) {
		List<Comment> comments = this.commentRepository.findByPostId(postId);
		return comments.stream().map(comment -> {
			GetAllCommentsForPostResponse commentResponse = this.modelMapperService.forResponse().map(comment,
					GetAllCommentsForPostResponse.class);
			commentResponse.setPostTitle(comment.getPost().getTitle());
			commentResponse.setAuthorUser(comment.getUser().getUsername());
			return commentResponse;
		}).collect(Collectors.toList());
	}

	@Override
	@Cacheable(value = "commentsForUser", key = "#userId")
	public List<GetAllCommentsForUserResponse> getAllForUser(Long userId) {
		List<Comment> comments = this.commentRepository.findByUserId(userId);
		return comments.stream()
				.map(comment -> this.modelMapperService.forResponse().map(comment, GetAllCommentsForUserResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	@Cacheable(value = "commentDetails", key = "#id")
	public GetByIdCommentResponse getById(Long id) {
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Comment not found!"));
		return this.modelMapperService.forResponse().map(comment, GetByIdCommentResponse.class);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "commentsForPost", key = "#postId"),
			@CacheEvict(value = "commentsForUser", key = "#user.id") })
	public Comment add(Long postId, CreateCommentRequest createCommentRequest, User user) {
		Comment comment = new Comment();
		comment.setText(createCommentRequest.getText());
		comment.setUser(user);
		comment.setPost(
				this.postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found!")));
		comment.setCreatedDate(new Date());

		Post post = comment.getPost();
		post.incrementCommentCount();
		this.postRepository.save(post);

		return this.commentRepository.save(comment);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "commentDetails", key = "#id"),
			@CacheEvict(value = "commentsForPost", allEntries = true),
			@CacheEvict(value = "commentsForUser", allEntries = true) })
	public Comment update(Long id, UpdateCommentRequest updateCommentRequest, User user) {
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Comment not found!"));
		if (!comment.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to update this comment.");
		}
		comment.setText(updateCommentRequest.getText());
		return this.commentRepository.save(comment);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "commentDetails", key = "#id"),
			@CacheEvict(value = "commentsForPost", allEntries = true),
			@CacheEvict(value = "commentsForUser", allEntries = true) })
	public void delete(Long id, User user) {
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Comment not found!"));
		if (!comment.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to delete this comment.");
		}
		Post post = comment.getPost();
		post.decrementCommentCount();
		this.postRepository.save(post);
		this.commentRepository.deleteById(id);
	}
}
