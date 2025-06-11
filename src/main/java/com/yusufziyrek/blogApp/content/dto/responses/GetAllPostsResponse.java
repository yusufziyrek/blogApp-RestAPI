package com.yusufziyrek.blogApp.content.dto.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllPostsResponse {

	private Long id;
	private String authorUser;
	private String title;
	private int commentCount;
	private int likeCount;
	private LocalDateTime createdDate;

}
