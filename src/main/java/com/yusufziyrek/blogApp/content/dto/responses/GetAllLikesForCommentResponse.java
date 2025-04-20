package com.yusufziyrek.blogApp.content.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllLikesForCommentResponse {

	private Long id;
	private String userWhoLiked;
	private String likedCommentText;

}
