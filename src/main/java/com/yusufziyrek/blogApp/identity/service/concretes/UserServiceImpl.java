package com.yusufziyrek.blogApp.identity.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.service.abstracts.IPostService;
import com.yusufziyrek.blogApp.identity.domain.User;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import com.yusufziyrek.blogApp.identity.service.abstracts.IUserService;
import com.yusufziyrek.blogApp.services.rules.UserServiceRules;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.shared.mapper.IModelMapperService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

	private final IUserRepository userRepository;
	private final IPostService postService;
	private final IModelMapperService modelMapperService;
	private final UserServiceRules serviceRules;

	@Override
	@Cacheable(value = "allUsers", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	public PageResponse<GetAllUsersResponse> getAll(Pageable pageable) {
		Page<User> users = this.userRepository.findAll(pageable);
		return toPageResponse(users, GetAllUsersResponse.class);
	}

	@Override
	@Cacheable(value = "userDetails", key = "#id")
	public GetByIdUserResponse getById(Long id) {
		User user = this.userRepository.findById(id).orElseThrow(() -> new UserException("User id not exist !"));

		GetByIdUserResponse response = this.modelMapperService.forResponse().map(user, GetByIdUserResponse.class);
		response.setTitles(this.postService.getPostTitleForUser(id));

		return response;
	}

	public GetByIdUserResponse getByUserName(String username) {
		User user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UserException("Username doesn't exist"));
		return this.modelMapperService.forResponse().map(user, GetByIdUserResponse.class);
	}

	@Override
	@CacheEvict(value = { "userDetails", "allUsers" }, allEntries = true)
	public User add(RegisterRequest registerUserRequest) {
		this.serviceRules.checkIfUserNameExists(registerUserRequest.getUsername());
		User user = this.modelMapperService.forRequest().map(registerUserRequest, User.class);
		return this.userRepository.save(user);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userDetails", key = "#id"),
			@CacheEvict(value = "allUsers", allEntries = true) })
	public User update(Long id, UpdateUserRequest updateUserRequest) {
		User user = this.userRepository.findById(id).orElseThrow(() -> new UserException("User id not exist !"));
		user.setUsername(updateUserRequest.getUsername());
		user.setFirstname(updateUserRequest.getFirstname());
		user.setLastname(updateUserRequest.getLastname());
		user.setPassword(updateUserRequest.getPassword());
		user.setDepartment(updateUserRequest.getDepartment());
		user.setAge(updateUserRequest.getAge());

		return this.userRepository.save(user);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userDetails", key = "#id"),
			@CacheEvict(value = "allUsers", allEntries = true) })
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
