package com.yusufziyrek.blogApp.content.dto.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentsForPostResponse {

	private Long id;
	private String text;
	private int likeCount;
	private LocalDateTime createdDate;
	private String postTitle;
	private String authorUser;

}
