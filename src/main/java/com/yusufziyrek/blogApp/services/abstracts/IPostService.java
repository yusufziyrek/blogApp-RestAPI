package com.yusufziyrek.blogApp.services.abstracts;

import java.util.List;

import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.services.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.services.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdPostResponse;

public interface IPostService {

	List<GetAllPostsResponse> getAll();

	GetByIdPostResponse getById(Long id);

	List<String> getPostForUser(Long userId);

	Post createPost(CreatePostRequest createPostRequest);

	Post update(Long id, UpdatePostRequest updatePostRequest);

	void delete(Long id);

}
