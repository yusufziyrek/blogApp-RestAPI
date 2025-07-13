package com.yusufziyrek.blogApp.content.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {

	@NotEmpty(message = "Title cannot be empty")
	@Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
	private String title;

	@NotEmpty(message = "Text cannot be empty")
	@Size(min = 10, max = 5000, message = "Text must be between 10 and 5000 characters")
	private String text;

}
