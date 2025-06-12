package com.yusufziyrek.blogApp.identity.service.concretes;

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
@CacheConfig(cacheNames = { "allUsers", "userDetails" }, keyGenerator = "cacheKeyGenerator")
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IPostService postService;
    private final UserServiceRules serviceRules;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable("allUsers")
    public PageResponse<GetAllUsersResponse> getAll(Pageable pageable) {
        var page = userRepository.findAll(pageable);
        var items = page.getContent().stream()
            .map(u -> GetAllUsersResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .department(u.getDepartment())
                .age(u.getAge())
                .build())
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
        var user = userRepository.findById(id)
            .orElseThrow(() -> new UserException("User id not exist !"));
        var titles = postService.getPostTitleForUser(id);

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
    @Cacheable(value = "userDetails", key = "#username")
    public GetByIdUserResponse getByUserName(String username) {
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserException("Username doesn't exist !"));
        var titles = postService.getPostTitleForUser(user.getId());

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
    @CacheEvict(value = "allUsers", allEntries = true)
    public User add(RegisterRequest req) {
        serviceRules.checkIfUserNameExists(req.getUsername());
        var user = User.builder()
            .firstname(req.getFirstname())
            .lastname(req.getLastname())
            .username(req.getUsername())
            .email(req.getEmail())
            .password(req.getPassword())
            .department(req.getDepartment())
            .age(req.getAge())
            .build();
        return userRepository.save(user);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "userDetails", key = "#id"),
        @CacheEvict(value = "allUsers", allEntries = true)
    })
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public User update(Long id, UpdateUserRequest req) {
    	User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User id not exist !"));

        String passwordToSave = req.getPassword() != null
                ? passwordEncoder.encode(req.getPassword())
                : existing.getPassword();

        User updated = existing.toBuilder()
                .firstname(  Objects.requireNonNullElse(req.getFirstname(),   existing.getFirstname()))
                .lastname(   Objects.requireNonNullElse(req.getLastname(),    existing.getLastname()))
                .username(   Objects.requireNonNullElse(req.getUsername(),    existing.getUsername()))
                .password(   passwordToSave)
                .department( Objects.requireNonNullElse(req.getDepartment(),  existing.getDepartment()))
                .age(        Objects.requireNonNullElse(req.getAge(),         existing.getAge()))
                .build();

        return userRepository.save(updated);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "userDetails", key = "#id"),
        @CacheEvict(value = "allUsers", allEntries = true)
    })
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void delete(Long id) {
    	userRepository.findById(id).orElseThrow(()-> new UserException("User id not exist !"));   	
        userRepository.deleteById(id);
    }
}
