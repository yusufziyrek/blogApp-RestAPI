package com.yusufziyrek.blogApp.identity.web.concretes;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.identity.dto.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.identity.service.abstracts.IUserService;
import com.yusufziyrek.blogApp.identity.web.abstracts.IUsersController;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.dto.ResponseMessages;

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
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.USERS_RETRIEVED_SUCCESSFULLY, users));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdUserResponse>> getProfileByUser(@AuthenticationPrincipal User user) {
		GetByIdUserResponse response= userService.getById(user.getId());
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.USER_RETRIEVED_SUCCESSFULLY, response));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdUserResponse>> getByUserName(@PathVariable String username) {
		GetByIdUserResponse user = userService.getByUserName(username);
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.USER_RETRIEVED_SUCCESSFULLY, user));
	}

	@Override
	public ResponseEntity<ApiResponse<GetByIdUserResponse>> getById(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id) {
		GetByIdUserResponse user = userService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.USER_RETRIEVED_SUCCESSFULLY, user));
	}

	@Override
	public ResponseEntity<ApiResponse<User>> update(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id,
			@RequestBody @Valid UpdateUserRequest updateUserRequest) {
		User updatedUser = userService.update(id, updateUserRequest);
		return ResponseEntity.ok(new ApiResponse<>(true, ResponseMessages.USER_UPDATED_SUCCESSFULLY, updatedUser));
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> delete(
			@PathVariable @Positive(message = "User ID must be a positive number") Long id) {
		userService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(new ApiResponse<>(true, ResponseMessages.USER_DELETED_SUCCESSFULLY, null));
	}
}