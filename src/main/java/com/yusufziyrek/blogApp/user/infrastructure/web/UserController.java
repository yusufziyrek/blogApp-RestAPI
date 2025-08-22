package com.yusufziyrek.blogApp.user.infrastructure.web;

import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.user.application.usecases.*;
import com.yusufziyrek.blogApp.user.dto.request.UpdateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.shared.security.UserPrincipal;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * UserDomain REST Controller
 * Clean Architecture - Infrastructure Layer (Web)
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    
    public UserController(GetUserByIdUseCase getUserByIdUseCase,
                         GetAllUsersUseCase getAllUsersUseCase,
                         UpdateUserProfileUseCase updateUserProfileUseCase) {
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.getAllUsersUseCase = getAllUsersUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal user) {
        UserResponse userResponse = getUserByIdUseCase.execute(user.getId());
        return ResponseEntity.ok(userResponse);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.id == #id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id, 
                                                   @AuthenticationPrincipal UserPrincipal user) {
        UserResponse userResponse = getUserByIdUseCase.execute(id);
        return ResponseEntity.ok(userResponse);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageResponse<UserResponse> response = getAllUsersUseCase.execute(page, size);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        UserResponse updatedUser = updateUserProfileUseCase.execute(user.getId(), request);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.id == #id")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        UserResponse updatedUser = updateUserProfileUseCase.execute(id, request);
        return ResponseEntity.ok(updatedUser);
    }
}
