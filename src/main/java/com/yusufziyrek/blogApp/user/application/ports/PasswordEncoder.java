package com.yusufziyrek.blogApp.user.application.ports;

/**
 * Port for Password Encoding
 * Clean Architecture - Application Layer
 */
public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
