package com.yusufziyrek.blogApp.services.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.entities.Like;
import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.repos.ICommentRepository;
import com.yusufziyrek.blogApp.repos.ILikeRepository;
import com.yusufziyrek.blogApp.repos.IPostRepository;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.services.abstracts.ILikeService;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdLikeResponse;
import com.yusufziyrek.blogApp.services.rules.LikeServiceRules;
import com.yusufziyrek.blogApp.utilites.mappers.IModelMapperService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeManager implements ILikeService {

	private final ILikeRepository likeRepository;
	private final IPostRepository postRepository;
	private final IUserRepository userRepository;
	private final ICommentRepository commentRepository;
	private final IModelMapperService modelMapperService;
	private final LikeServiceRules serviceRules;

	@Override
	public List<GetAllLikesForPostResponse> getAllForPost(Long postId) {

		List<Like> likes = this.likeRepository.findByPostId(postId);

		List<GetAllLikesForPostResponse> response = likes.stream()
				.map(like -> this.modelMapperService.forResponse().map(like, GetAllLikesForPostResponse.class))
				.collect(Collectors.toList());

		return response;
	}

	@Override
	public List<GetAllLikesForCommentResponse> getAllLikesForComment(Long commentId) {

		List<Like> likes = this.likeRepository.findByCommentId(commentId);

		List<GetAllLikesForCommentResponse> response = likes.stream()
				.map(like -> this.modelMapperService.forResponse().map(like, GetAllLikesForCommentResponse.class))
				.collect(Collectors.toList());

		return response;
	}

	@Override
	public GetByIdLikeResponse getById(Long id) {

		Like like = this.likeRepository.findById(id).orElseThrow();

		GetByIdLikeResponse response = this.modelMapperService.forResponse().map(like, GetByIdLikeResponse.class);

		return response;
	}

	@Override
	public Like addLikeForPost(Long postId, CreateLikeForPostRequest createLikeForPostRequest) {

		this.serviceRules.checkIfLikeAlreadyExistForPost(createLikeForPostRequest.getUserId(), postId);

		Like like = new Like();
		like.setUser(this.userRepository.findById(createLikeForPostRequest.getUserId()).orElseThrow());
		like.setPost(this.postRepository.findById(postId).orElseThrow());

		Post post = like.getPost();
		post.incrementLikeCount();
		this.postRepository.save(post);

		return this.likeRepository.save(like);

	}

	@Override
	public Like addLikeForComment(Long commentId, CreateLikeForCommentRequest createLikeForCommentRequest) {

		this.serviceRules.checkIfLikeAlreadyExistForComment(createLikeForCommentRequest.getUserId(), commentId);

		Like like = new Like();
		like.setUser(this.userRepository.findById(createLikeForCommentRequest.getUserId()).orElseThrow());
		like.setComment(this.commentRepository.findById(commentId).orElseThrow());

		Comment comment = like.getComment();
		comment.incrementLikeCount();
		this.commentRepository.save(comment);

		return this.likeRepository.save(like);
	}

	@Override
	public void dislikeForPost(Long id) {

		Post post = this.likeRepository.findById(id).orElseThrow().getPost();
		post.decrementLikeCount();
		this.postRepository.save(post);

		this.likeRepository.deleteById(id);
	}

	@Override
	public void dislikeForComment(Long id) {

		Comment comment = this.likeRepository.findById(id).orElseThrow().getComment();
		comment.decrementLikeCount();
		this.commentRepository.save(comment);

		this.likeRepository.deleteById(id);

	}

}
