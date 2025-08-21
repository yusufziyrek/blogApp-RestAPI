package com.yusufziyrek.blogApp.user.infrastructure.web;

import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.user.application.usecases.*;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.user.dto.request.CreateUserRequest;
import com.yusufziyrek.blogApp.user.dto.request.UpdateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.user.dto.response.UserSummaryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final CreateUserUseCase createUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = createUserUseCase.execute(
            request.getFirstname(),
            request.getLastname(),
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getDepartment(),
            request.getAge()
        );
        
        return ResponseEntity.ok(mapToUserResponse(user));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.id == #id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        User foundUser = getUserByIdUseCase.execute(id);
        return ResponseEntity.ok(mapToUserResponse(foundUser));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserSummaryResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = getAllUsersUseCase.execute(pageable);
        
        PageResponse<UserSummaryResponse> response = new PageResponse<>();
        response.setItems(users.getContent().stream()
            .map(this::mapToUserSummaryResponse)
            .toList());
        response.setPage(users.getNumber());
        response.setSize(users.getSize());
        response.setTotalItems(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.id == #id")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal User user) {
        
        User updatedUser = updateUserProfileUseCase.execute(
            id,
            request.getFirstname(),
            request.getLastname(),
            request.getEmail(),
            request.getDepartment(),
            request.getAge(),
            request.getPassword()
        );
        
        return ResponseEntity.ok(mapToUserResponse(updatedUser));
    }
    
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setDepartment(user.getDepartment());
        response.setAge(user.getAge());
        response.setRole(user.getRole());
        response.setEnabled(user.isEnabled());
        response.setCreatedDate(user.getCreatedDate());
        response.setUpdatedDate(user.getUpdatedDate());
        return response;
    }
    
    private UserSummaryResponse mapToUserSummaryResponse(User user) {
        UserSummaryResponse response = new UserSummaryResponse();
        response.setId(user.getId());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
