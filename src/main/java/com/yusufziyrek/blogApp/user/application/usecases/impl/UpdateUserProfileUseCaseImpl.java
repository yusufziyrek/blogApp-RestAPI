package com.yusufziyrek.blogApp.user.application.usecases.impl;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.UpdateUserProfileUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.request.UpdateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.shared.exception.UserException;

/**
 * Update profil use case implementasyonu
 */
public class UpdateUserProfileUseCaseImpl implements UpdateUserProfileUseCase {
    
    private final UserRepository userRepository;
    
    public UpdateUserProfileUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserResponse execute(Long userId, UpdateUserRequest request) {
        UserDomain user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException("UserDomain not found with id: " + userId));
        
    // E-posta değişiyorsa mevcut mu diye bak
        if (request.getEmail() != null && 
            !user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email already exists: " + request.getEmail());
        }
        
    // Profil güncellemesini domain metoduyla yap
        user.updateProfile(
            request.getFirstname(),
            request.getLastname(),
            request.getEmail(),
            request.getDepartment(),
            request.getAge()
        );
        
    // Güncelleme sonrası iş kurallarını doğrula
        if (!user.hasValidEmail()) {
            throw new UserException("Invalid email format");
        }
        
        if (!user.hasValidAge()) {
            throw new UserException("Invalid age");
        }
        
    // Güncelleneni kaydet
        UserDomain updatedUser = userRepository.save(user);
        
        return mapToResponse(updatedUser);
    }
    
    private UserResponse mapToResponse(UserDomain user) {
        return new UserResponse(
            user.getId(),
            user.getFirstname(),
            user.getLastname(),
            user.getUsername(),
            user.getEmail(),
            user.getDepartment(),
            user.getAge(),
            user.getRole(),
            user.isEnabled(),
            user.getCreatedDate(),
            user.getUpdatedDate()
        );
    }
}
