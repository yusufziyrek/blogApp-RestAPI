package com.yusufziyrek.blogApp.content.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

	@NotEmpty(message = "Text cannot be empty")
	@Size(min = 10, max = 1000, message = "Text must be between 10 and 1000 characters")
	private String text;

}
