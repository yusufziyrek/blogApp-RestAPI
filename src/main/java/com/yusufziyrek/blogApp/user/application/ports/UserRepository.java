package com.yusufziyrek.blogApp.user.application.ports;

import com.yusufziyrek.blogApp.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository {
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteById(Long id);
}
