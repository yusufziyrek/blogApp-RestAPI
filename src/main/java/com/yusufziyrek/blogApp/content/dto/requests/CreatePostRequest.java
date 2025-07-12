package com.yusufziyrek.blogApp.content.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {

	@NotBlank(message = "Title cannot be empty")
	@Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
	private String title;

	@NotBlank(message = "Text cannot be empty")
	@Size(min = 10, max = 10000, message = "Text must be between 10 and 10000 characters")
	private String text;
}
