package com.yusufziyrek.blogApp.identity.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.identity.domain.rules.UserServiceRules;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.identity.mapper.UserMapper;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import com.yusufziyrek.blogApp.identity.service.abstracts.IUserService;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

	private final IUserRepository userRepository;
	private final UserMapper userMapper;
	private final UserServiceRules serviceRules;

	@Override
	@Cacheable(value = "allUsers", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	public PageResponse<GetAllUsersResponse> getAll(Pageable pageable) {
		Page<User> users = this.userRepository.findAll(pageable);
		return toPageResponse(users);
	}

	@Override
	@Cacheable(value = "userDetails", key = "#id")
	public GetByIdUserResponse getById(Long id) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, id)));
		return this.userMapper.toGetByIdResponse(user);
	}

	public GetByIdUserResponse getByUserName(String username) {
		User user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_USERNAME, username)));
		return this.userMapper.toGetByIdResponse(user);
	}

	@Override
	@CacheEvict(value = { "userDetails", "allUsers" }, allEntries = true)
	public User add(RegisterRequest registerUserRequest) {
		this.serviceRules.checkIfUserNameExists(registerUserRequest.getUsername());
		User user = this.userMapper.toUser(registerUserRequest);
		return this.userRepository.save(user);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userDetails", key = "#id"),
			@CacheEvict(value = "allUsers", allEntries = true) })
	public User update(Long id, UpdateUserRequest updateUserRequest) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, id)));
		this.userMapper.updateUserFromRequest(user, updateUserRequest);
		return this.userRepository.save(user);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userDetails", key = "#id"),
			@CacheEvict(value = "allUsers", allEntries = true) })
	public void delete(Long id) {
		this.userRepository.deleteById(id);
	}

	private PageResponse<GetAllUsersResponse> toPageResponse(Page<User> source) {
		List<GetAllUsersResponse> items = source.getContent().stream()
				.map(userMapper::toGetAllUsersResponse).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}
}
