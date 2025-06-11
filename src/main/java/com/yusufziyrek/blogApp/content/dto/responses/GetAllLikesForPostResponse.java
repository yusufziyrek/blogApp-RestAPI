package com.yusufziyrek.blogApp.content.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllLikesForPostResponse {

	private Long id;
	private String userWhoLiked;
	private String likedPostTitle;

}
