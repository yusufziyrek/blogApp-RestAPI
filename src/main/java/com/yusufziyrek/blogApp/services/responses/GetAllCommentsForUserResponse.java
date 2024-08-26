package com.yusufziyrek.blogApp.services.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentsForUserResponse {

	private Long id;
	private Long postId;
	private Long userId;
	private String text;
	private int likeCount;
	private Date createDate;

}
