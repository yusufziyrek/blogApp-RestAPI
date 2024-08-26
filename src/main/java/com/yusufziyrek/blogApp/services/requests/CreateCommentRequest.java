package com.yusufziyrek.blogApp.services.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

	@NotNull(message = "post id cannot be null")
	@Min(value = 1, message = "post id must be at least 1 !")
	private Long postId;

	@NotNull(message = "user id cannot be null")
	@Min(value = 1, message = "user id must be at least 1 !")
	private Long userId;

	@NotEmpty(message = "text cannot be null")
	@Size(min = 2, max = 50, message = "text size must be between 10 and 50 characters !")
	private String text;

}
