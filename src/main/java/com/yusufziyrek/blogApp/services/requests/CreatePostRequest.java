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
public class CreatePostRequest {

	@NotNull(message = "user id cannot be null")
	@Min(value = 1, message = "user id must be at least 1 !")
	private Long userId;

	@NotEmpty(message = "title cannot be null")
	@Size(min = 2, max = 20, message = "title size must be between 2 and 20 characters !")
	private String title;

	@NotEmpty(message = "text cannot be null !")
	@Size(min = 10, max = 200, message = "text size must be between 10 and 200 characters !")
	private String text;

}
