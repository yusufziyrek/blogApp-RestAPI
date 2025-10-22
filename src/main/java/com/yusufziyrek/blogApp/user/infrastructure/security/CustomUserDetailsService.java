package com.yusufziyrek.blogApp.user.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.security.UserPrincipal;
import com.yusufziyrek.blogApp.user.domain.Role;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.JpaUserRepository;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Kullanıcı yükleme servisi — girişte JPA'daki kullanıcı verisini kullanıyorum
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    
    private final JpaUserRepository jpaUserRepository;
    
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.info("Loading user by username or email: {}", usernameOrEmail);
        
        UserEntity user = jpaUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> {
                log.warn("UserDomain not found with username or email: {}", usernameOrEmail);
                return new UsernameNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_BY_USERNAME_OR_EMAIL, usernameOrEmail));
            });
        
        log.info("UserDomain loaded successfully: {}", user.getEmail());
        
    // UserEntity'yi UserPrincipal'e çeviriyorum (auth için)
        return new UserPrincipal(
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            user.getPassword(),
            Role.valueOf(user.getRole().name()), // Role dönüşümü
            true // aktif
        );
    }
}
