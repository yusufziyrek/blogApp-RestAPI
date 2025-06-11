package com.yusufziyrek.blogApp.content.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllCommentsForPostResponse {

	private Long id;
	private String postTitle;
	private String authorUser;
	private String text;
	private int likeCount;
	private Date createDate;

}
