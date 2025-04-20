package com.yusufziyrek.blogApp.content.dto.responses;

import java.util.List;

import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;

import lombok.Data;

@Data
public class SearchResultResponse {
	private List<GetAllPostsResponse> blogPosts;
	private List<GetByIdUserResponse> users;
}
