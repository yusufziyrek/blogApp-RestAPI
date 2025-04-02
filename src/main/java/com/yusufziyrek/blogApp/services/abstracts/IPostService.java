package com.yusufziyrek.blogApp.services.abstracts;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.services.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.services.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;

public interface IPostService {

	PageResponse<GetAllPostsResponse> getAll(Pageable pageable);

	List<String> getPostForUser(Long userId);

	GetByIdPostResponse getById(Long id);

	Post createPost(CreatePostRequest createPostRequest);

	Post update(Long id, UpdatePostRequest updatePostRequest);

	Long delete(Long id);

}
