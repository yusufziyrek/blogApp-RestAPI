package com.yusufziyrek.blogApp.user.infrastructure.security;

import com.yusufziyrek.blogApp.user.application.ports.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Password Encoder Adapter
 * Clean Architecture - Infrastructure Layer
 * Adapts Spring Security PasswordEncoder to Domain Port
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoder {
    
    private final org.springframework.security.crypto.password.PasswordEncoder springPasswordEncoder;
    
    public PasswordEncoderAdapter(org.springframework.security.crypto.password.PasswordEncoder springPasswordEncoder) {
        this.springPasswordEncoder = springPasswordEncoder;
    }
    
    @Override
    public String encode(String rawPassword) {
        return springPasswordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
