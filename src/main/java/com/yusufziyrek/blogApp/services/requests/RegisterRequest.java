package com.yusufziyrek.blogApp.services.requests;

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
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@NotEmpty(message = "First name cannot be null")
	@Size(min = 2, max = 20, message = "First name size must be between 2 and 15 characters")
	private String firstname;

	@NotEmpty(message = "Last name cannot be null")
	@Size(min = 2, max = 20, message = "Last name size must be between 2 and 10 characters")
	private String lastname;

	@NotBlank(message = "Username cannot be empty")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String username;

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password cannot be empty")
	@Size(min = 6, max = 20, message = "Password must be at least 6 characters")
	private String password;

	@NotEmpty(message = "Department cannot be null")
	@Size(min = 2, max = 25, message = "Department size must be between 2 and 25 characters")
	private String department;

	@NotNull(message = "Age cannot be null")
	@Min(value = 18, message = "The age value must be at least 18")
	@Max(value = 100, message = "The age value must be at most 100")
	private int age;
}