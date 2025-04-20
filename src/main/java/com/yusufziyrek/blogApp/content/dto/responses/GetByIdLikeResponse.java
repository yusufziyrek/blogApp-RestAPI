package com.yusufziyrek.blogApp.content.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdLikeResponse {

	private Long id;
	private String username;
	private String postTitle;
	private Long commentId;

}
