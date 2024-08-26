package com.yusufziyrek.blogApp.services.abstracts;

import java.util.List;

import com.yusufziyrek.blogApp.entities.Like;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.services.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdLikeResponse;

public interface ILikeService {

	List<GetAllLikesForPostResponse> getAllForPost(Long postId);

	List<GetAllLikesForCommentResponse> getAllLikesForComment(Long commentId);

	GetByIdLikeResponse getById(Long id);

	Like addLikeForPost(Long postId, CreateLikeForPostRequest createLikeForPostRequest);

	Like addLikeForComment(Long commentId, CreateLikeForCommentRequest createLikeForCommentRequest);

	void dislikeForPost(Long id);

	void dislikeForComment(Long id);

}
