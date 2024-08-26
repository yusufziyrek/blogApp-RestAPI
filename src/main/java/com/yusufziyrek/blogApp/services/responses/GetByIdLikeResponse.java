package com.yusufziyrek.blogApp.services.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdLikeResponse {

	private Long id;
	private String userName;
	private String postTitle;
	private Long commentId;

}
