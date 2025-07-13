package com.yusufziyrek.blogApp.identity.service.abstracts;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

public interface IUserService {

	PageResponse<GetAllUsersResponse> getAll(Pageable pageable);

	GetByIdUserResponse getById(Long id);

	GetByIdUserResponse getByUserName(String username);

	User add(RegisterRequest registerUserRequest);

	User update(Long id, UpdateUserRequest updateUserRequest);

	void delete(Long id);

}
