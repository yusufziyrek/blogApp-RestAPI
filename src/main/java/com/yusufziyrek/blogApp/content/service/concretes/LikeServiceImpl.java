package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.domain.Comment;
import com.yusufziyrek.blogApp.content.domain.Like;
import com.yusufziyrek.blogApp.content.domain.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdLikeResponse;
import com.yusufziyrek.blogApp.content.repo.ICommentRepository;
import com.yusufziyrek.blogApp.content.repo.ILikeRepository;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ILikeService;
import com.yusufziyrek.blogApp.identity.domain.User;
import com.yusufziyrek.blogApp.services.rules.LikeServiceRules;
import com.yusufziyrek.blogApp.shared.exception.LikeException;
import com.yusufziyrek.blogApp.shared.mapper.IModelMapperService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements ILikeService {

	private final ILikeRepository likeRepository;
	private final IPostRepository postRepository;
	private final ICommentRepository commentRepository;
	private final IModelMapperService modelMapperService;
	private final LikeServiceRules serviceRules;

	@Override
	public List<GetAllLikesForPostResponse> getAllForPost(Long postId) {
	    List<Like> likes = this.likeRepository.findByPostId(postId);
	    return likes.stream()
	            .map(like -> {
	                GetAllLikesForPostResponse response = this.modelMapperService.forResponse()
	                        .map(like, GetAllLikesForPostResponse.class);
	                response.setUserWhoLiked(like.getUser().getUsername());
	                return response;
	            })
	            .collect(Collectors.toList());
	}

	@Override
	public List<GetAllLikesForCommentResponse> getAllLikesForComment(Long commentId) {
	    List<Like> likes = this.likeRepository.findByCommentId(commentId);
	    return likes.stream()
	            .map(like -> {
	                GetAllLikesForCommentResponse response = this.modelMapperService.forResponse()
	                        .map(like, GetAllLikesForCommentResponse.class);
	                response.setUserWhoLiked(like.getUser().getUsername());
	                return response;
	            })
	            .collect(Collectors.toList());
	}

	@Override
	public GetByIdLikeResponse getById(Long id) {
		Like like = this.likeRepository.findById(id).orElseThrow();
		return this.modelMapperService.forResponse().map(like, GetByIdLikeResponse.class);
	}

	@Override
	public Like addLikeForPost(Long postId, CreateLikeForPostRequest createLikeForPostRequest, User user) {
		serviceRules.checkIfLikeAlreadyExistForPost(user.getId(), postId);

		Like like = new Like();
		like.setUser(user);
		like.setPost(this.postRepository.findById(postId).orElseThrow(() -> new LikeException("Post not found!")));

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
				this.commentRepository.findById(commentId).orElseThrow(() -> new LikeException("Comment not found!")));

		Comment comment = like.getComment();
		comment.incrementLikeCount();
		this.commentRepository.save(comment);

		return this.likeRepository.save(like);
	}

	@Override
	public void dislikeForPost(Long likeId, User user) {
		Like like = this.likeRepository.findById(likeId).orElseThrow(() -> new RuntimeException("Like not found!"));
		if (!like.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to remove this like.");
		}
		Post post = like.getPost();
		post.decrementLikeCount();
		this.postRepository.save(post);
		this.likeRepository.deleteById(likeId);
	}

	@Override
	public void dislikeForComment(Long likeId, User user) {
		Like like = this.likeRepository.findById(likeId).orElseThrow(() -> new RuntimeException("Like not found!"));
		if (!like.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to remove this like.");
		}
		Comment comment = like.getComment();
		comment.decrementLikeCount();
		this.commentRepository.save(comment);
		this.likeRepository.deleteById(likeId);
	}
}
