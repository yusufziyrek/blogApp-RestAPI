package com.yusufziyrek.blogApp.user.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UpdateUserProfileUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User execute(Long userId, String firstname, String lastname, 
                       String email, String department, int age, String password) {
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException("User not found with id: " + userId));
        
        // Check if email is changing and new email already exists
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new UserException("Email already exists");
        }
        
        // Update profile
        user.updateProfile(firstname, lastname, email, department, age);
        
        // Update password if provided
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        
        return userRepository.save(user);
    }
}
