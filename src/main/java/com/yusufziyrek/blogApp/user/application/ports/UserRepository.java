package com.yusufziyrek.blogApp.user.application.ports;

import com.yusufziyrek.blogApp.user.domain.UserDomain;
import java.util.List;
import java.util.Optional;

/**
 * Port (Interface) for UserDomain Repository
 * No framework dependencies - Pure domain interface
 */
public interface UserRepository {
    List<UserDomain> findAll(int page, int size);
    Optional<UserDomain> findById(Long id);
    Optional<UserDomain> findByUsername(String username);
    Optional<UserDomain> findByEmail(String email);
    Optional<UserDomain> findByUsernameOrEmail(String username, String email);
    UserDomain save(UserDomain user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteById(Long id);
    long getTotalCount();
}
