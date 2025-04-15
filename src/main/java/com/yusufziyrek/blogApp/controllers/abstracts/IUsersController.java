package com.yusufziyrek.blogApp.controllers.abstracts;

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

import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Validated
@RequestMapping("/api/v1/users")
public interface IUsersController {

	@GetMapping
	ResponseEntity<ApiResponse<PageResponse<GetAllUsersResponse>>> getAll(Pageable pageable);

	@GetMapping("/me")
	ResponseEntity<ApiResponse<GetByIdUserResponse>> getProfileByUser(@AuthenticationPrincipal User user);

	@GetMapping("/by-username/{username}")
	ResponseEntity<ApiResponse<GetByIdUserResponse>> getByUserName(@PathVariable String username);

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
