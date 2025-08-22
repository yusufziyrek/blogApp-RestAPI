package com.yusufziyrek.blogApp.user.infrastructure.persistence;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.infrastructure.mappers.UserMapper;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository Adapter Implementation
 * Clean Architecture - Infrastructure Layer
 * Adapts JPA Repository to Domain Repository Port
 */
@Component
public class UserRepositoryImpl implements UserRepository {
    
    private final JpaUserRepository jpaUserRepository;
    
    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }
    
    @Override
    public UserDomain save(UserDomain user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return UserMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<UserDomain> findById(Long id) {
        return jpaUserRepository.findById(id)
            .map(UserMapper::toDomain);
    }
    
    @Override
    public Optional<UserDomain> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username)
            .map(UserMapper::toDomain);
    }
    
    @Override
    public Optional<UserDomain> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
            .map(UserMapper::toDomain);
    }
    
    @Override
    public Optional<UserDomain> findByUsernameOrEmail(String username, String email) {
        return jpaUserRepository.findByUsernameOrEmail(username, email)
            .map(UserMapper::toDomain);
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
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }
    
    @Override
    public List<UserDomain> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> entityPage = jpaUserRepository.findAll(pageable);
        
        return entityPage.getContent()
            .stream()
            .map(UserMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public long getTotalCount() {
        return jpaUserRepository.count();
    }
}
