package com.yusufziyrek.blogApp.services.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLikeForPostRequest {

	@NotNull(message = "user id cannot be null !")
	@Min(value = 1, message = "user id must be at least 1 !")
	private Long userId;

	/*
	 * @NotNull(message = "post id cannot be null !")
	 * 
	 * @Min(value = 1, message = "post id must be at least 1 !") private Long
	 * postId;
	 */

}
