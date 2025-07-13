package com.yusufziyrek.blogApp.content.dto.responses;

import java.util.List;

import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultResponse {

	private List<GetAllPostsResponse> blogPosts;
	private List<GetByIdUserResponse> users;

}
