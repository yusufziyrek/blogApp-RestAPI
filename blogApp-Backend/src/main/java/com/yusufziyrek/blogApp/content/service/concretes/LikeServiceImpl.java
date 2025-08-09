package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.domain.models.Comment;
import com.yusufziyrek.blogApp.content.domain.models.Like;
import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.domain.rules.LikeServiceRules;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdLikeResponse;
import com.yusufziyrek.blogApp.content.mapper.LikeMapper;
import com.yusufziyrek.blogApp.content.repo.ICommentRepository;
import com.yusufziyrek.blogApp.content.repo.ILikeRepository;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ILikeService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.LikeException;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements ILikeService {

	private final ILikeRepository likeRepository;
	private final IPostRepository postRepository;
	private final ICommentRepository commentRepository;
	private final LikeMapper likeMapper;
	private final LikeServiceRules serviceRules;

	@Override
	public List<GetAllLikesForPostResponse> getAllForPost(Long postId) {
	    List<Like> likes = this.likeRepository.findByPostId(postId);
	    return this.likeMapper.toGetAllLikesForPostResponseList(likes);
	}

	@Override
	public List<GetAllLikesForCommentResponse> getAllLikesForComment(Long commentId) {
	    List<Like> likes = this.likeRepository.findByCommentId(commentId);
	    return this.likeMapper.toGetAllLikesForCommentResponseList(likes);
	}

	@Override
	public GetByIdLikeResponse getById(Long id) {
		Like like = this.likeRepository.findById(id)
				.orElseThrow(() -> new LikeException(String.format(ErrorMessages.LIKE_NOT_FOUND_BY_ID, id)));
		return this.likeMapper.toGetByIdResponse(like);
	}

	@Override
	public Like addLikeForPost(Long postId, CreateLikeForPostRequest createLikeForPostRequest, User user) {
		// Check if user has already liked this post
		if (likeRepository.existsByUserIdAndPostId(user.getId(), postId)) {
			throw new LikeException("You have already liked this post");
		}
		
		serviceRules.validateLikeData(postId, null);

		Like like = new Like();
		like.setUser(user);
		like.setPost(this.postRepository.findById(postId).orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, postId))));

		Like savedLike = this.likeRepository.save(like);
		
		// Recalculate like count to ensure consistency
		recalculateLikeCount(postId);
		
		return savedLike;
	}

	@Override
	public Like addLikeForComment(Long commentId, CreateLikeForCommentRequest createLikeForCommentRequest, User user) {
		serviceRules.checkIfLikeAlreadyExistForComment(user.getId(), commentId);
		serviceRules.validateLikeData(null, commentId);

		Like like = new Like();
		like.setUser(user);
		like.setComment(
				this.commentRepository.findById(commentId).orElseThrow(() -> new CommentException(String.format(ErrorMessages.COMMENT_NOT_FOUND_BY_ID, commentId))));

		Comment comment = like.getComment();
		comment.incrementLikeCount();
		this.commentRepository.save(comment);

		return this.likeRepository.save(like);
	}

	@Override
	public void unlikePost(Long postId, User user) {
		// Check if user has liked this post
		if (!likeRepository.existsByUserIdAndPostId(user.getId(), postId)) {
			throw new LikeException("User has not liked this post");
		}
		
		Like like = this.likeRepository.findByUserIdAndPostId(user.getId(), postId)
				.orElseThrow(() -> new LikeException("User has not liked this post"));
		
		// Delete the like
		this.likeRepository.deleteById(like.getId());
		
		// Recalculate like count to ensure consistency
		recalculateLikeCount(postId);
	}

	// Helper method to recalculate like count for a post
	private void recalculateLikeCount(Long postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, postId)));
		
		long actualLikeCount = this.likeRepository.findByPostId(postId).size();
		post.setLikeCount((int) actualLikeCount);
		this.postRepository.save(post);
	}
}
