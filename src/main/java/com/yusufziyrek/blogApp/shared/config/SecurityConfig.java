package com.yusufziyrek.blogApp.shared.config;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.yusufziyrek.blogApp.shared.security.CustomAccessDeniedHandler;
import com.yusufziyrek.blogApp.shared.security.CustomAuthEntryPoint;
import com.yusufziyrek.blogApp.shared.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    private static final String API_BASE = "/api/v1";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
           .cors(cors -> cors.configurationSource(corsConfigurationSource()))
           .csrf(csrf -> csrf.disable())
           .authorizeHttpRequests(auth -> auth
               .requestMatchers(API_BASE + "/auth/**").permitAll()
               .requestMatchers(HttpMethod.GET,
                   API_BASE + "/posts/**",
                   API_BASE + "/comments/**",
                   API_BASE + "/likes/**",
                   API_BASE + "/search/**",
                   API_BASE + "/stats/**").permitAll()
               .requestMatchers(API_BASE + "/users/**").authenticated()
               .requestMatchers(HttpMethod.POST,
                   API_BASE + "/posts/**",
                   API_BASE + "/comments/**",
                   API_BASE + "/likes/**").authenticated()
               .requestMatchers(HttpMethod.PUT,
                   API_BASE + "/posts/**",
                   API_BASE + "/comments/**").authenticated()
               .requestMatchers(HttpMethod.DELETE,
                   API_BASE + "/posts/**",
                   API_BASE + "/comments/**",
                   API_BASE + "/likes/**").authenticated()
               .requestMatchers(API_BASE + "/admin/**").hasRole("ADMIN")
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
