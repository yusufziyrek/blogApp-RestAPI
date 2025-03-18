package com.yusufziyrek.blogApp.services.requests;

import jakarta.validation.constraints.Max;
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
public class UpdateUserRequest {

	@NotEmpty(message = "first name cannot be null")
	@Size(min = 2, max = 10, message = "first name size must be between 2 and 10 characters !")
	private String firstname;

	@NotEmpty(message = "last name cannot be null")
	@Size(min = 2, max = 10, message = "last name size must be between 2 and 10 characters !")
	private String lastname;

	@NotEmpty(message = "username cannot be null")
	@Size(min = 2, max = 20, message = "username size must be between 2 and 10 characters !")
	private String username;

	@NotEmpty(message = "password cannot be null")
	@Size(min = 2, max = 10, message = "password name size must be between 2 and 10 characters !")
	private String password;

	@NotEmpty(message = "departmant cannot be null")
	@Size(min = 2, max = 20, message = "departman size must be between 2 and 10 characters !")
	private String departmant;

	@NotNull(message = "age cannot be null")
	@Min(value = 18, message = "the age value must be at least 18 !")
	@Max(value = 100, message = "the age value must be at most 100 !")
	private int age;
}
