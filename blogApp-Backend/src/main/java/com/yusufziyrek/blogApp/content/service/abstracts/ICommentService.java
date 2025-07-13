package com.yusufziyrek.blogApp.content.service.abstracts;

import java.util.List;

import com.yusufziyrek.blogApp.content.domain.models.Comment;
import com.yusufziyrek.blogApp.content.dto.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdCommentResponse;
import com.yusufziyrek.blogApp.identity.domain.models.User;

public interface ICommentService {

	List<GetAllCommentsForPostResponse> getAllForPost(Long postId);

	List<GetAllCommentsForUserResponse> getAllForUser(Long userId);

	GetByIdCommentResponse getById(Long id);

	Comment add(Long postId, CreateCommentRequest createCommentRequest, User user);

	Comment update(Long id, UpdateCommentRequest updateCommentRequest, User user);

	void delete(Long id, User user);

}
