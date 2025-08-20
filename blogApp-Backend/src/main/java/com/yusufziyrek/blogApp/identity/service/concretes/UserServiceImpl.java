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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

	private final IUserRepository userRepository;
	private final UserMapper userMapper;
	private final UserServiceRules serviceRules;

	@Override
	@Cacheable(value = "allUsers", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	public PageResponse<GetAllUsersResponse> getAll(Pageable pageable) {
		log.info("Getting all users - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<User> users = this.userRepository.findAll(pageable);
		log.info("Found {} users", users.getTotalElements());
		return toPageResponse(users);
	}

	@Override
	@Cacheable(value = "userDetails", key = "#id")
	public GetByIdUserResponse getById(Long id) {
		log.info("Getting user by ID: {}", id);
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, id)));
		log.info("Successfully found user: {}", user.getUsername());
		return this.userMapper.toGetByIdResponse(user);
	}

	public GetByIdUserResponse getByUserName(String username) {
		log.info("Getting user by username: {}", username);
		User user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_USERNAME, username)));
		log.info("Successfully found user by username: {}", username);
		return this.userMapper.toGetByIdResponse(user);
	}

	@Override
	@CacheEvict(value = { "userDetails", "allUsers" }, allEntries = true)
	public User add(RegisterRequest registerUserRequest) {
		log.info("Adding new user with username: {}", registerUserRequest.getUsername());
		this.serviceRules.checkIfUserNameExists(registerUserRequest.getUsername());
		User user = this.userMapper.toUser(registerUserRequest);
		User savedUser = this.userRepository.save(user);
		log.info("Successfully added user with ID: {}", savedUser.getId());
		return savedUser;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userDetails", key = "#id"),
			@CacheEvict(value = "allUsers", allEntries = true) })
	public User update(Long id, UpdateUserRequest updateUserRequest) {
		log.info("Updating user with ID: {}", id);
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, id)));
		this.userMapper.updateUserFromRequest(user, updateUserRequest);
		User updatedUser = this.userRepository.save(user);
		log.info("Successfully updated user with ID: {}", id);
		return updatedUser;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userDetails", key = "#id"),
			@CacheEvict(value = "allUsers", allEntries = true) })
	public void delete(Long id) {
		log.info("Deleting user with ID: {}", id);
		this.userRepository.deleteById(id);
		log.info("Successfully deleted user with ID: {}", id);
	}

	private PageResponse<GetAllUsersResponse> toPageResponse(Page<User> source) {
		List<GetAllUsersResponse> items = source.getContent().stream()
				.map(userMapper::toGetAllUsersResponse).collect(Collectors.toList());

		return new PageResponse<>(items, source.getNumber(), source.getSize(), source.getTotalElements(),
				source.getTotalPages());
	}
}
