package com.yusufziyrek.blogApp.identity.service.concretes;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
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
            .orElseThrow(() -> new UserException("Username doesn't exist"));
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
    public User update(Long id, UpdateUserRequest req) {
        var existing = userRepository.findById(id)
            .orElseThrow(() -> new UserException("User id not exist !"));
        var updated = existing.toBuilder()
            .firstname(req.getFirstname()   != null ? req.getFirstname()   : existing.getFirstname())
            .lastname(req.getLastname()     != null ? req.getLastname()    : existing.getLastname())
            .username(req.getUsername()     != null ? req.getUsername()    : existing.getUsername())
            .password(req.getPassword()     != null ? req.getPassword()    : existing.getPassword())
            .department(req.getDepartment() != null ? req.getDepartment()  : existing.getDepartment())
            .age(req.getAge()               != null ? req.getAge()         : existing.getAge())
            .build();
        return userRepository.save(updated);
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
