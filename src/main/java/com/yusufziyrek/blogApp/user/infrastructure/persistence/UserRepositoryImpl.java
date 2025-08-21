package com.yusufziyrek.blogApp.user.infrastructure.persistence;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final JpaUserRepository jpaUserRepository;
    
    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
    
    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpaUserRepository.findAll(pageable);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }
}
