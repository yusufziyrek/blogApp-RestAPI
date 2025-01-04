package com.yusufziyrek.blogApp.services.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.services.abstracts.IPostService;
import com.yusufziyrek.blogApp.services.abstracts.IUserService;
import com.yusufziyrek.blogApp.services.requests.CreateUserRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.services.responses.PageResponse;
import com.yusufziyrek.blogApp.services.rules.UserServiceRules;
import com.yusufziyrek.blogApp.utilites.exceptions.UserException;
import com.yusufziyrek.blogApp.utilites.mappers.IModelMapperService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserManager implements IUserService {

	private IUserRepository userRepository;
	private IPostService postService;
	private IModelMapperService modelMapperService;
	private UserServiceRules serviceRules;

	@Override
	public PageResponse<GetAllUsersResponse> getAll(Pageable pageable) {
		Page<User> products = this.userRepository.findAll(pageable);
		return toPageResponse(products, GetAllUsersResponse.class);
	}

	@Override
	public GetByIdUserResponse getById(Long id) {

		User user = this.userRepository.findById(id).orElseThrow(() -> new UserException("User id not exist !"));

		GetByIdUserResponse response = this.modelMapperService.forResponse().map(user, GetByIdUserResponse.class);

		response.setTitles(this.postService.getPostForUser(id));

		return response;
	}

	@Override
	public User add(CreateUserRequest creatUserRequest) {

		this.serviceRules.checkIfUserNameExists(creatUserRequest.getUserName());

		User user = this.modelMapperService.forRequest().map(creatUserRequest, User.class);

		return this.userRepository.save(user);

	}

	@Override
	public User update(Long id, UpdateUserRequest updateUserRequest) {

		User user = this.userRepository.findById(id).orElseThrow(() -> new UserException("User id not exist !"));
		user.setUserName(updateUserRequest.getUserName());
		user.setFirstName(updateUserRequest.getFirstName());
		user.setLastName(updateUserRequest.getLastName());
		user.setPassword(updateUserRequest.getPassword());
		user.setDepartmant(updateUserRequest.getDepartmant());
		user.setAge(updateUserRequest.getAge());

		return this.userRepository.save(user);

	}

	@Override
	public void delete(Long id) {

		this.userRepository.deleteById(id);

	}

	private <T, U> PageResponse<U> toPageResponse(Page<T> source, Class<U> targetClass) {
		List<U> items = source.getContent().stream()
				.map(item -> modelMapperService.forResponse().map(item, targetClass)).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}
}
