package com.yusufziyrek.blogApp.services.abstracts;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.services.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;

public interface IPostService {

	PageResponse<GetAllPostsResponse> getAll(Pageable pageable);
	
	PageResponse<GetAllPostsResponse> getAllForUser(Pageable pageable, Long userId);
	
	List<String> getPostTitleForUser(Long userId);

	GetByIdPostResponse getById(Long id);

	Post createPost(CreatePostRequest createPostRequest, User user);

	Post update(Long id, UpdatePostRequest updatePostRequest, User user);

	Long delete(Long id, User user);
}
