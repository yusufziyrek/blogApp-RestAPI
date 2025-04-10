package com.yusufziyrek.blogApp.services.abstracts;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.RegisterRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;

public interface IUserService {

	PageResponse<GetAllUsersResponse> getAll(Pageable pageable);

	GetByIdUserResponse getById(Long id);

	GetByIdUserResponse getByUserName(String username);

	User add(RegisterRequest registerUserRequest);

	User update(Long id, UpdateUserRequest updateUserRequest);

	void delete(Long id);

}
