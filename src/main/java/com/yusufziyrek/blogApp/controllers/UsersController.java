package com.yusufziyrek.blogApp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.abstracts.IUserService;
import com.yusufziyrek.blogApp.services.requests.CreateUserRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdUserResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@Validated
@AllArgsConstructor
public class UsersController {

	private IUserService userService;

	@GetMapping
	public List<GetAllUsersResponse> getAll() {
		return this.userService.getAll();
	}

	@GetMapping("/{id}")
	public GetByIdUserResponse getById(@PathVariable Long id) {
		return this.userService.getById(id);

	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public User add(@RequestBody @Valid CreateUserRequest createUserRequest) {
		return this.userService.add(createUserRequest);
	}

	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public User update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
		return this.userService.update(id, updateUserRequest);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		this.userService.delete(id);

	}

}
