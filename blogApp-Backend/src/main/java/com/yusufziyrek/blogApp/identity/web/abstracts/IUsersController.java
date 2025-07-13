package com.yusufziyrek.blogApp.identity.web.abstracts;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.identity.dto.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Validated
@RequestMapping("/api/v1/users")
public interface IUsersController {

	@GetMapping
	ResponseEntity<ApiResponse<PageResponse<GetAllUsersResponse>>> getAll(Pageable pageable);

	@GetMapping("/me")
	ResponseEntity<ApiResponse<GetByIdUserResponse>> getProfileByUser(@AuthenticationPrincipal User user);

	@GetMapping("/by-username/{username}")
	ResponseEntity<ApiResponse<GetByIdUserResponse>> getByUserName(@PathVariable @NotBlank(message = "Username cannot be empty") String username);

	@GetMapping("/{id}")
	ResponseEntity<ApiResponse<GetByIdUserResponse>> getById(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id);

	@PutMapping("/{id}")
	ResponseEntity<ApiResponse<User>> update(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id,
			@RequestBody @Valid UpdateUserRequest updateUserRequest);

	@DeleteMapping("/{id}")
	ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id);
}
