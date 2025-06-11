package com.yusufziyrek.blogApp.identity.dto.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 10, message = "First name must be 2–10 chars")
    private String firstname;

    @Size(min = 2, max = 10, message = "Last name must be 2–10 chars")
    private String lastname;

    @Size(min = 3, max = 20, message = "Username must be 3–20 chars")
    private String username;

    @Size(min = 6, max = 50, message = "Password must be 6–50 chars")
    private String password;

    @Size(min = 2, max = 20, message = "Department must be 2–20 chars")
    private String department;

    @Min(value = 18, message = "Age must be ≥18")
    @Max(value = 100, message = "Age must be ≤100")
    private Integer age;

}