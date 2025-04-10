package com.yusufziyrek.blogApp.controllers.concretes;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.controllers.abstracts.IUsersController;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.abstracts.IUserService;
import com.yusufziyrek.blogApp.services.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
public class UsersController implements IUsersController {

	private final IUserService userService;

	@Override
	public ResponseEntity<ApiResponse<PageResponse<GetAllUsersResponse>>> getAll(Pageable pageable) {
		PageResponse<GetAllUsersResponse> users = userService.getAll(pageable);
		return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdUserResponse>> getByUserName(String username) {
		GetByIdUserResponse user = userService.getByUserName(username);
		return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdUserResponse>> getById(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id) {
		GetByIdUserResponse user = userService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user));
	}

	@Override
	public ResponseEntity<ApiResponse<User>> update(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id,
			@RequestBody @Valid UpdateUserRequest updateUserRequest) {
		User updatedUser = userService.update(id, updateUserRequest);
		return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", updatedUser));
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id) {
		userService.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
	}
}