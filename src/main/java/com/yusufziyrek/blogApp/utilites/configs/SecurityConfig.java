package com.yusufziyrek.blogApp.utilites.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.yusufziyrek.blogApp.security.JwtAuthenticationFilter;
import com.yusufziyrek.blogApp.utilites.exceptions.CustomAccessDeniedHandler;
import com.yusufziyrek.blogApp.utilites.exceptions.CustomAuthEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
           .authorizeHttpRequests(auth -> auth
               .requestMatchers("/api/auth/**").permitAll()
               .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/comments/**", "/api/likes/**").permitAll()
               .requestMatchers(HttpMethod.POST, "/api/posts/**", "/api/comments/**", "/api/likes/**").authenticated()
               .requestMatchers(HttpMethod.PUT, "/api/posts/**", "/api/comments/**").authenticated()
               .requestMatchers(HttpMethod.DELETE, "/api/posts/**", "/api/comments/**", "/api/likes/**").authenticated()
               .requestMatchers("/api/users/**").authenticated()
               .requestMatchers("/api/admin/**").hasRole("ADMIN")
               .anyRequest().authenticated()
           )
           .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .exceptionHandling(exception -> exception
               .authenticationEntryPoint(customAuthEntryPoint)
               .accessDeniedHandler(customAccessDeniedHandler)
           )
           .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
