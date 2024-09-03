package com.yusufziyrek.blogApp.services.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLikeForCommentRequest {

	@NotNull(message = "user id cannot be null !")
	@Min(value = 1, message = "user id must be at least 1 !")
	private Long userId;

	/*
	 * @NotNull(message = "comment id cannot be null !")
	 * 
	 * @Min(value = 1, message = "comment id must be at least 1 !") private Long
	 * commentId;
	 */

}
