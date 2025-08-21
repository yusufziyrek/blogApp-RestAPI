package com.yusufziyrek.blogApp.user.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserByIdUseCase {
    
    private final UserRepository userRepository;
    
    public User execute(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException("User not found with id: " + userId));
    }
}
