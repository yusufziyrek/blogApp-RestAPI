package com.yusufziyrek.blogApp.services.abstracts;

import java.util.List;

import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.CreateUserRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdUserResponse;

public interface IUserService {

	List<GetAllUsersResponse> getAll();

	GetByIdUserResponse getById(Long id);

	User add(CreateUserRequest creatUserRequest);

	User update(Long id, UpdateUserRequest updateUserRequest);

	void delete(Long id);

}
