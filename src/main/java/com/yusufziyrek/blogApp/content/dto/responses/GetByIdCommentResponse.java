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
public class GetByIdCommentResponse {

	private Long id;
	private Long postId;
	private Long userId;
	private String text;
	private int likeCount;
	private Date createdDate;

}
