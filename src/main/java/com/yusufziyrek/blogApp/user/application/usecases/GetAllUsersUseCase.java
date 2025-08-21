package com.yusufziyrek.blogApp.user.application.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllUsersUseCase {
    
    private final UserRepository userRepository;
    
    public Page<User> execute(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
