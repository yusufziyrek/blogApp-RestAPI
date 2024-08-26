package com.yusufziyrek.blogApp.services.concretes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.repos.ICommentRepository;
import com.yusufziyrek.blogApp.repos.IPostRepository;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.services.abstracts.ICommentService;
import com.yusufziyrek.blogApp.services.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdCommentResponse;
import com.yusufziyrek.blogApp.utilites.mappers.IModelMapperService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentManager implements ICommentService {

	private ICommentRepository commentRepository;
	private IUserRepository userRepository;
	private IPostRepository postRepository;
	private IModelMapperService modelMapperService;

	@Override
	public List<GetAllCommentsForPostResponse> getAllForPost(Long postId) {

		List<Comment> comments = this.commentRepository.findByPostId(postId);
		List<GetAllCommentsForPostResponse> response = comments.stream().map(comment -> {
			GetAllCommentsForPostResponse commentResponse = this.modelMapperService.forResponse().map(comment,
					GetAllCommentsForPostResponse.class);

			commentResponse.setPostTitle(comment.getPost().getTitle());
			commentResponse.setAuthorUser(comment.getUser().getUserName());
			return commentResponse;
		}).collect(Collectors.toList());

		return response;
	}

	@Override
	public List<GetAllCommentsForUserResponse> getAllForUser(Long userId) {

		List<Comment> comments = this.commentRepository.findByUserId(userId);
		List<GetAllCommentsForUserResponse> response = comments.stream()
				.map(comment -> this.modelMapperService.forResponse().map(comment, GetAllCommentsForUserResponse.class))
				.collect(Collectors.toList());

		return response;

	}

	@Override
	public GetByIdCommentResponse getById(Long id) {

		Comment comment = this.commentRepository.findById(id).orElseThrow();
		GetByIdCommentResponse response = this.modelMapperService.forResponse().map(comment,
				GetByIdCommentResponse.class);

		return response;
	}

	@Override
	public Comment add(CreateCommentRequest createCommentRequest) {

		Comment comment = new Comment();
		comment.setText(createCommentRequest.getText());
		comment.setUser(this.userRepository.findById(createCommentRequest.getUserId()).orElseThrow());
		comment.setPost(this.postRepository.findById(createCommentRequest.getPostId()).orElseThrow());
		comment.setCreatedDate(new Date());

		Post post = comment.getPost();
		post.incrementCommentCount();

		this.postRepository.save(post);

		return this.commentRepository.save(comment);
	}

	@Override
	public Comment update(Long id, UpdateCommentRequest updateCommentRequest) {

		Comment comment = this.commentRepository.findById(id).orElseThrow();
		comment.setText(updateCommentRequest.getText());

		return this.commentRepository.save(comment);
	}

	@Override
	public void delete(Long id) {

		Post post = this.commentRepository.findById(id).orElseThrow().getPost();
		post.decrementCommentCount();
		this.postRepository.save(post);

		this.commentRepository.deleteById(id);

	}

}
