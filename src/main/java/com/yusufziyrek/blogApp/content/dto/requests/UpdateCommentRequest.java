package com.yusufziyrek.blogApp.content.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCommentRequest {

	@NotEmpty(message = "text cannot be null")
	@Size(min = 2, max = 50, message = "text size must be between 10 and 200 characters !")
	private String text;

}
