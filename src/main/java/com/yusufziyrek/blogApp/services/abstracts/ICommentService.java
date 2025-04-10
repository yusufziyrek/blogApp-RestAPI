package com.yusufziyrek.blogApp.services.abstracts;

import java.util.List;

import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdCommentResponse;

public interface ICommentService {

	List<GetAllCommentsForPostResponse> getAllForPost(Long postId);

	List<GetAllCommentsForUserResponse> getAllForUser(Long userId);

	GetByIdCommentResponse getById(Long id);

	Comment add(Long postId, CreateCommentRequest createCommentRequest, User user);

	Comment update(Long id, UpdateCommentRequest updateCommentRequest, User user);

	void delete(Long id, User user);

}
