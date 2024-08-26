package com.yusufziyrek.blogApp.services.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdPostResponse {

	private Long id;
	private String authorUser;
	private String title;
	private String text;
	private int commentCount;
	private int likeCount;;
	private Date createdDate;

}
