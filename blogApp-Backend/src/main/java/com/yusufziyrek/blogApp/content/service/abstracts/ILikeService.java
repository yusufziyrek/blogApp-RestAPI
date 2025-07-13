package com.yusufziyrek.blogApp.content.service.abstracts;

import java.util.List;

import com.yusufziyrek.blogApp.content.domain.models.Like;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdLikeResponse;
import com.yusufziyrek.blogApp.identity.domain.models.User;

public interface ILikeService {

	List<GetAllLikesForPostResponse> getAllForPost(Long postId);

	List<GetAllLikesForCommentResponse> getAllLikesForComment(Long commentId);

	GetByIdLikeResponse getById(Long id);

	Like addLikeForPost(Long postId, CreateLikeForPostRequest createLikeForPostRequest, User user);

	Like addLikeForComment(Long commentId, CreateLikeForCommentRequest createLikeForCommentRequest, User user);

	void dislikeForPost(Long likeId, User user);

	void dislikeForComment(Long likeId, User user);
}
