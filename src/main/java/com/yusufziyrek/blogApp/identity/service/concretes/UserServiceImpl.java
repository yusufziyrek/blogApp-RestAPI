package com.yusufziyrek.blogApp.identity.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.service.abstracts.IPostService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.identity.domain.rules.UserServiceRules;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.UpdateUserRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.GetAllUsersResponse;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import com.yusufziyrek.blogApp.identity.service.abstracts.IUserService;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IPostService postService;
    private final UserServiceRules serviceRules;

    @Override
    @Cacheable(value = "allUsers", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public PageResponse<GetAllUsersResponse> getAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        List<GetAllUsersResponse> items = usersPage.getContent().stream()
            .map(user -> GetAllUsersResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .department(user.getDepartment())
                .age(user.getAge())
                .build())
            .collect(Collectors.toList());

        return new PageResponse<>(
            items,
            usersPage.getNumber(),
            usersPage.getSize(),
            usersPage.getTotalElements(),
            usersPage.getTotalPages()
        );
    }

    @Override
    @Cacheable(value = "userDetails", key = "#id")
    public GetByIdUserResponse getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserException("User id not exist !"));

        List<String> titles = postService.getPostTitleForUser(id);

        return GetByIdUserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .department(user.getDepartment())
            .age(user.getAge())
            .titles(titles)
            .build();
    }

    public GetByIdUserResponse getByUserName(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserException("Username doesn't exist"));

        List<String> titles = postService.getPostTitleForUser(user.getId());

        return GetByIdUserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .department(user.getDepartment())
            .age(user.getAge())
            .titles(titles)
            .build();
    }

    @Override
    @CacheEvict(value = { "userDetails", "allUsers" }, allEntries = true)
    public User add(RegisterRequest registerRequest) {
        serviceRules.checkIfUserNameExists(registerRequest.getUsername());

        User user = User.builder()
            .firstname(registerRequest.getFirstname())
            .lastname(registerRequest.getLastname())
            .username(registerRequest.getUsername())
            .email(registerRequest.getEmail())
            .password(registerRequest.getPassword())
            .department(registerRequest.getDepartment())
            .age(registerRequest.getAge())
            .build();

        return userRepository.save(user);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "userDetails", key = "#id"),
        @CacheEvict(value = "allUsers", allEntries = true)
    })
    public User update(Long id, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserException("User id not exist !"));

        user.setFirstname(updateRequest.getFirstname());
        user.setLastname(updateRequest.getLastname());
        user.setUsername(updateRequest.getUsername());
        user.setPassword(updateRequest.getPassword());
        user.setDepartment(updateRequest.getDepartment());
        user.setAge(updateRequest.getAge());

        return userRepository.save(user);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "userDetails", key = "#id"),
        @CacheEvict(value = "allUsers", allEntries = true)
    })
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}