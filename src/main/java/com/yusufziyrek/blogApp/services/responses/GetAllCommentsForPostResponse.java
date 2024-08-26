package com.yusufziyrek.blogApp.services.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentsForPostResponse {

	private Long id;
	private String postTitle;
	private String authorUser;
	private String text;
	private int likeCount;
	private Date createDate;

}
