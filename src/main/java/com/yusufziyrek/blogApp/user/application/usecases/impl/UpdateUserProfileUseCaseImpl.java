package com.yusufziyrek.blogApp.user.application.usecases.impl;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.UpdateUserProfileUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.request.UpdateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

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
            .orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, userId), HttpStatus.NOT_FOUND));
        
        // E-posta değişiyorsa mevcut mu diye bak
        if (request.getEmail() != null && 
            !user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new UserException(String.format(ErrorMessages.EMAIL_ALREADY_EXISTS, request.getEmail()), HttpStatus.CONFLICT);
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
            throw new UserException(ErrorMessages.INVALID_EMAIL_FORMAT);
        }
        
        if (!user.hasValidAge()) {
            throw new UserException(ErrorMessages.AGE_RANGE_INVALID);
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
