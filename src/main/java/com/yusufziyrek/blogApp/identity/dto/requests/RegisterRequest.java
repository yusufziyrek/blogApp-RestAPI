package com.yusufziyrek.blogApp.identity.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@NotEmpty(message = "First name cannot be empty")
	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	private String firstname;

	@NotEmpty(message = "Last name cannot be empty")
	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
	private String lastname;

	@NotBlank(message = "Username cannot be empty")
	@Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
	private String username;

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password cannot be empty")
	@Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
	private String password;

	@NotEmpty(message = "Department cannot be empty")
	@Size(min = 2, max = 100, message = "Department must be between 2 and 100 characters")
	private String department;

	@NotNull(message = "Age cannot be null")
	@Min(value = 18, message = "Age must be at least 18")
	@Max(value = 100, message = "Age must be at most 100")
	private int age;
}