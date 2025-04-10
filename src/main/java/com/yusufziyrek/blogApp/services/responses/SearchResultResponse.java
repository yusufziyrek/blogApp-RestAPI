package com.yusufziyrek.blogApp.services.responses;

import java.util.List;

import lombok.Data;

@Data
public class SearchResultResponse {
	private List<GetAllPostsResponse> blogPosts;
	private List<GetByIdUserResponse> users;
}
