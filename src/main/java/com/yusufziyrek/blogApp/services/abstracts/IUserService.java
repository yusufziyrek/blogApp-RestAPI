package com.yusufziyrek.blogApp.services.abstracts;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.services.requests.CreateUserRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;

public interface IUserService {

	PageResponse<GetAllUsersResponse> getAll(Pageable pageable);

	GetByIdUserResponse getById(Long id);

	User add(CreateUserRequest creatUserRequest);

	User update(Long id, UpdateUserRequest updateUserRequest);

	void delete(Long id);

}
