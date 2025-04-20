package com.yusufziyrek.blogApp.content.service.abstracts;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

public interface IPostService {

	PageResponse<GetAllPostsResponse> getAll(Pageable pageable);
	
	PageResponse<GetAllPostsResponse> getAllForUser(Pageable pageable, Long userId);
	
	List<String> getPostTitleForUser(Long userId);

	GetByIdPostResponse getById(Long id);

	Post createPost(CreatePostRequest createPostRequest, User user);

	Post update(Long id, UpdatePostRequest updatePostRequest, User user);

	Long delete(Long id, User user);
}
