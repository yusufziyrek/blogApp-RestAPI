package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.List;

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
import com.yusufziyrek.blogApp.content.mapper.CommentMapper;
import com.yusufziyrek.blogApp.content.repo.ICommentRepository;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ICommentService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements ICommentService {

	private final ICommentRepository commentRepository;
	private final IPostRepository postRepository;
	private final CommentMapper commentMapper;

	@Override
	@Cacheable(value = "commentsForPost", key = "#postId")
	public List<GetAllCommentsForPostResponse> getAllForPost(Long postId) {
		log.info("Getting all comments for post ID: {}", postId);
		List<Comment> comments = this.commentRepository.findByPostId(postId);
		log.info("Found {} comments for post ID: {}", comments.size(), postId);
		return this.commentMapper.toGetAllCommentsForPostResponseList(comments);
	}

	@Override
	@Cacheable(value = "commentsForUser", key = "#userId")
	public List<GetAllCommentsForUserResponse> getAllForUser(Long userId) {
		log.info("Getting all comments for user ID: {}", userId);
		List<Comment> comments = this.commentRepository.findByUserId(userId);
		log.info("Found {} comments for user ID: {}", comments.size(), userId);
		return this.commentMapper.toGetAllCommentsForUserResponseList(comments);
	}

	@Override
	@Cacheable(value = "commentDetails", key = "#id")
	public GetByIdCommentResponse getById(Long id) {
		log.info("Getting comment by ID: {}", id);
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new CommentException(String.format(ErrorMessages.COMMENT_NOT_FOUND_BY_ID, id)));
		log.info("Successfully found comment with ID: {}", id);
		return this.commentMapper.toGetByIdResponse(comment);
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "commentsForPost", key = "#postId"),
		@CacheEvict(value = "commentsForUser", key = "#user.id"),
		@CacheEvict(value = "postDetails", key = "#postId")
	})
	public Comment add(Long postId, CreateCommentRequest createCommentRequest, User user) {
		log.info("Adding comment to post ID: {} by user: {}", postId, user.getUsername());
		Comment comment = this.commentMapper.toComment(createCommentRequest);
		comment.setUser(user);
		comment.setPost(
				this.postRepository.findById(postId).orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, postId))));

		Post post = comment.getPost();
		post.incrementCommentCount();
		this.postRepository.save(post);

		Comment savedComment = this.commentRepository.save(comment);
		log.info("Successfully added comment with ID: {} to post ID: {}", savedComment.getId(), postId);
		return savedComment;
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "commentDetails", key = "#id"),
		@CacheEvict(value = "commentsForPost", allEntries = true),
		@CacheEvict(value = "commentsForUser", allEntries = true) 
	})
	public Comment update(Long id, UpdateCommentRequest updateCommentRequest, User user) {
		log.info("Updating comment with ID: {} by user: {}", id, user.getUsername());
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new CommentException(String.format(ErrorMessages.COMMENT_NOT_FOUND_BY_ID, id)));
		if (!comment.getUser().getId().equals(user.getId())) {
			log.warn("Access denied: User {} tried to update comment {} owned by another user", user.getUsername(), id);
			throw new AccessDeniedException(ErrorMessages.COMMENT_ACCESS_DENIED_UPDATE);
		}
		this.commentMapper.updateCommentFromRequest(comment, updateCommentRequest);
		Comment updatedComment = this.commentRepository.save(comment);
		log.info("Successfully updated comment with ID: {}", id);
		return updatedComment;
	}

	@Override
	@Caching(evict = { 
		@CacheEvict(value = "commentDetails", key = "#id"),
		@CacheEvict(value = "commentsForPost", allEntries = true),
		@CacheEvict(value = "commentsForUser", allEntries = true),
		@CacheEvict(value = "postDetails", key = "#comment.post.id")
	})
	public void delete(Long id, User user) {
		log.info("Deleting comment with ID: {} by user: {}", id, user.getUsername());
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new CommentException(String.format(ErrorMessages.COMMENT_NOT_FOUND_BY_ID, id)));
		if (!comment.getUser().getId().equals(user.getId())) {
			log.warn("Access denied: User {} tried to delete comment {} owned by another user", user.getUsername(), id);
			throw new AccessDeniedException(ErrorMessages.COMMENT_ACCESS_DENIED_DELETE);
		}
		Post post = comment.getPost();
		post.decrementCommentCount();
		this.postRepository.save(post);
		this.commentRepository.deleteById(id);
		log.info("Successfully deleted comment with ID: {}", id);
	}
}
