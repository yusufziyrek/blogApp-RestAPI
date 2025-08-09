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
public class GetByIdPostResponse {

	private Long id;
	private String title;
	private String text;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private int commentCount;
	private int likeCount;
	private String authorUser;
	private Long authorId;

}
