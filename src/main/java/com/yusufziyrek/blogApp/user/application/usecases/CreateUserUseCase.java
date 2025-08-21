package com.yusufziyrek.blogApp.user.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class CreateUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User execute(String firstname, String lastname, String username, 
                       String email, String password, String department, int age) {
        
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new UserException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new UserException("Email already exists");
        }
        
        // Create user
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setDepartment(department);
        user.setAge(age);
        user.setRole(Role.USER); // Default role
        user.setEnabled(false); // Needs verification
        
        return userRepository.save(user);
    }
}
