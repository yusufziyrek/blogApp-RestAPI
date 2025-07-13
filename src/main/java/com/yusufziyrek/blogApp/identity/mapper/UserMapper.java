package com.yusufziyrek.blogApp.identity.mapper;

import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

	public GetByIdUserResponse toGetByIdResponse(User user) {
		if (user == null)
			return null;

		GetByIdUserResponse response = new GetByIdUserResponse();
		response.setId(user.getId());
		response.setFirstname(user.getFirstname());
		response.setLastname(user.getLastname());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setDepartment(user.getDepartment());
		response.setAge(user.getAge());
		return response;
	}

	public GetAllUsersResponse toGetAllUsersResponse(User user) {
		if (user == null)
			return null;

		GetAllUsersResponse response = new GetAllUsersResponse();
		response.setId(user.getId());
		response.setFirstname(user.getFirstname());
		response.setLastname(user.getLastname());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setDepartment(user.getDepartment());
		response.setAge(user.getAge());
		return response;
	}

	public List<GetAllUsersResponse> toGetAllUsersResponseList(List<User> users) {
		if (users == null)
			return null;

		return users.stream().map(this::toGetAllUsersResponse).collect(Collectors.toList());
	}

	public User toUser(RegisterRequest request) {
		if (request == null)
			return null;

		User user = new User();
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setEmail(request.getEmail());
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		user.setDepartment(request.getDepartment());
		user.setAge(request.getAge());
		return user;
	}

	public void updateUserFromRequest(User user, UpdateUserRequest request) {
		if (user == null || request == null)
			return;

		if (request.getFirstname() != null) {
			user.setFirstname(request.getFirstname());
		}
		if (request.getLastname() != null) {
			user.setLastname(request.getLastname());
		}
		if (request.getUsername() != null) {
			user.setUsername(request.getUsername());
		}
		if (request.getPassword() != null) {
			user.setPassword(request.getPassword());
		}
		if (request.getDepartment() != null) {
			user.setDepartment(request.getDepartment());
		}
		user.setAge(request.getAge());
	}
}