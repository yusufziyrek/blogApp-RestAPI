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
		serviceRules.checkIfLikeAlreadyExistForPost(user.getId(), postId);

		Like like = new Like();
		like.setUser(user);
		like.setPost(this.postRepository.findById(postId).orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, postId))));

		Post post = like.getPost();
		post.incrementLikeCount();
		this.postRepository.save(post);

		return this.likeRepository.save(like);
	}

	@Override
	public Like addLikeForComment(Long commentId, CreateLikeForCommentRequest createLikeForCommentRequest, User user) {
		serviceRules.checkIfLikeAlreadyExistForComment(user.getId(), commentId);

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
	public void dislikeForPost(Long likeId, User user) {
		Like like = this.likeRepository.findById(likeId).orElseThrow(() -> new LikeException(String.format(ErrorMessages.LIKE_NOT_FOUND_BY_ID, likeId)));
		if (!like.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException(ErrorMessages.LIKE_ACCESS_DENIED_DELETE);
		}
		Post post = like.getPost();
		post.decrementLikeCount();
		this.postRepository.save(post);
		this.likeRepository.deleteById(likeId);
	}

	@Override
	public void dislikeForComment(Long likeId, User user) {
		Like like = this.likeRepository.findById(likeId).orElseThrow(() -> new LikeException(String.format(ErrorMessages.LIKE_NOT_FOUND_BY_ID, likeId)));
		if (!like.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException(ErrorMessages.LIKE_ACCESS_DENIED_DELETE);
		}
		Comment comment = like.getComment();
		comment.decrementLikeCount();
		this.commentRepository.save(comment);
		this.likeRepository.deleteById(likeId);
	}
}
