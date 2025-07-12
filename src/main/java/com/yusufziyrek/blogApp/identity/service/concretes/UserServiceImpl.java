package com.yusufziyrek.blogApp.identity.service.concretes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.content.service.abstracts.IPostService;
import com.yusufziyrek.blogApp.identity.domain.models.Role;
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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = { "allUsers", "userDetails" }, keyGenerator = "cacheKeyGenerator")
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IPostService postService;
    private final UserServiceRules serviceRules;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable("allUsers")
    public PageResponse<GetAllUsersResponse> getAll(Pageable pageable) {
        log.debug("Getting all users with pagination: {}", pageable);
        
        var page = userRepository.findAll(pageable);
        var items = page.getContent().stream()
            .map(this::mapToGetAllUsersResponse)
            .collect(Collectors.toList());

        return new PageResponse<>(
            items,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    @Cacheable("userDetails")
    public GetByIdUserResponse getById(Long id) {
        log.debug("Getting user by id: {}", id);
        
        var user = userRepository.findById(id)
            .orElseThrow(() -> new UserException("User id not exist !"));
        var titles = postService.getPostTitleForUser(id);

        return mapToGetByIdUserResponse(user, titles);
    }

    @Override
    @Cacheable("userDetails")
    public GetByIdUserResponse getByUserName(String username) {
        log.debug("Getting user by username: {}", username);
        
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserException("User not found with username: " + username));
        var titles = postService.getPostTitleForUser(user.getId());

        return mapToGetByIdUserResponse(user, titles);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "allUsers", allEntries = true),
        @CacheEvict(value = "userDetails", allEntries = true)
    })
    public User add(RegisterRequest registerUserRequest) {
        log.info("Adding new user with username: {}", registerUserRequest.getUsername());
        
        serviceRules.checkIfUserExists(registerUserRequest.getUsername(), registerUserRequest.getEmail());

        User user = User.builder()
            .firstname(registerUserRequest.getFirstname())
            .lastname(registerUserRequest.getLastname())
            .username(registerUserRequest.getUsername())
            .email(registerUserRequest.getEmail())
            .password(passwordEncoder.encode(registerUserRequest.getPassword()))
            .department(registerUserRequest.getDepartment())
            .age(registerUserRequest.getAge())
            .role(Role.USER)
            .enabled(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        
        return savedUser;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "userDetails", key = "#id"),
        @CacheEvict(value = "allUsers", allEntries = true)
    })
    public User update(Long id, UpdateUserRequest updateUserRequest) {
        log.info("Updating user with id: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserException("User id not exist !"));

        if (Objects.nonNull(updateUserRequest.getFirstname())) {
            user.setFirstname(updateUserRequest.getFirstname());
        }
        if (Objects.nonNull(updateUserRequest.getLastname())) {
            user.setLastname(updateUserRequest.getLastname());
        }
        if (Objects.nonNull(updateUserRequest.getDepartment())) {
            user.setDepartment(updateUserRequest.getDepartment());
        }
        if (Objects.nonNull(updateUserRequest.getAge())) {
            user.setAge(updateUserRequest.getAge());
        }
        if (Objects.nonNull(updateUserRequest.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id: {}", updatedUser.getId());
        
        return updatedUser;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Caching(evict = {
        @CacheEvict(value = "userDetails", key = "#id"),
        @CacheEvict(value = "allUsers", allEntries = true)
    })
    public void delete(Long id) {
        log.info("Deleting user with id: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new UserException("User id not exist !");
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    // Helper methods for mapping
    private GetAllUsersResponse mapToGetAllUsersResponse(User user) {
        return GetAllUsersResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .department(user.getDepartment())
            .age(user.getAge())
            .build();
    }

    private GetByIdUserResponse mapToGetByIdUserResponse(User user, java.util.List<String> titles) {
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
}
