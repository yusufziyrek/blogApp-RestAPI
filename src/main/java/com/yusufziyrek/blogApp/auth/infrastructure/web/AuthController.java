package com.yusufziyrek.blogApp.auth.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.auth.dto.request.LoginRequest;
import com.yusufziyrek.blogApp.auth.dto.request.RefreshTokenRequest;
import com.yusufziyrek.blogApp.auth.dto.response.AuthResponse;
import com.yusufziyrek.blogApp.auth.application.usecases.CreateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.ValidateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.RevokeRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshToken;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByEmailUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.CreateUserUseCase;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.user.dto.request.CreateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final CreateRefreshTokenUseCase createRefreshTokenUseCase;
    private final ValidateRefreshTokenUseCase validateRefreshTokenUseCase;
    private final RevokeRefreshTokenUseCase revokeRefreshTokenUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody CreateUserRequest registerRequest,
            HttpServletRequest request) {
        try {
            // Create new user
            User newUser = createUserUseCase.execute(
                registerRequest.getFirstname(),
                registerRequest.getLastname(),
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getDepartment(),
                registerRequest.getAge()
            );

            // Generate access token
            String accessToken = jwtUtil.generateToken(newUser.getEmail(), newUser.getId());
            
            // Create refresh token with device info
            String deviceInfo = extractDeviceInfo(request);
            String ipAddress = extractIpAddress(request);
            RefreshToken refreshToken = createRefreshTokenUseCase.execute(newUser, deviceInfo, ipAddress);

            // Create response
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(accessToken);
            authResponse.setRefreshToken(refreshToken.getToken());
            authResponse.setUser(mapToUserResponse(newUser));
            authResponse.setTokenType("Bearer");

            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user details
            User user = getUserByEmailUseCase.execute(loginRequest.getEmail());
            
            // Generate access token
            String accessToken = jwtUtil.generateToken(user.getEmail(), user.getId());
            
            // Create refresh token with device info
            String deviceInfo = extractDeviceInfo(request);
            String ipAddress = extractIpAddress(request);
            RefreshToken refreshToken = createRefreshTokenUseCase.execute(user, deviceInfo, ipAddress);

            // Create response
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(accessToken);
            authResponse.setRefreshToken(refreshToken.getToken());
            authResponse.setUser(mapToUserResponse(user));
            authResponse.setTokenType("Bearer");

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshRequest,
            HttpServletRequest request) {
        try {
            String tokenValue = refreshRequest.getRefreshToken();
            
            // Validate refresh token using use case
            RefreshToken refreshToken = validateRefreshTokenUseCase.execute(tokenValue);
            User user = refreshToken.getUser();
            
            // Revoke old token
            revokeRefreshTokenUseCase.execute(tokenValue);
            
            // Generate new access token
            String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getId());
            
            // Create new refresh token
            String deviceInfo = extractDeviceInfo(request);
            String ipAddress = extractIpAddress(request);
            RefreshToken newRefreshToken = createRefreshTokenUseCase.execute(user, deviceInfo, ipAddress);

            // Create response
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(newAccessToken);
            authResponse.setRefreshToken(newRefreshToken.getToken());
            authResponse.setUser(mapToUserResponse(user));
            authResponse.setTokenType("Bearer");

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String token = authHeader.substring(7);
            
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String email = jwtUtil.extractEmail(token);
            User user = getUserByEmailUseCase.execute(email);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setUser(mapToUserResponse(user));
            authResponse.setTokenType("Bearer");
            authResponse.setAccessToken(token);

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        try {
            String tokenValue = refreshRequest.getRefreshToken();
            revokeRefreshTokenUseCase.execute(tokenValue);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authHeader.substring(7);
            
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String email = jwtUtil.extractEmail(token);
            User user = getUserByEmailUseCase.execute(email);
            
            revokeRefreshTokenUseCase.executeAllByUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private String extractDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown Device";
        }
        
        // Simple device detection - in production you'd use a proper library
        if (userAgent.contains("Mobile")) {
            return "Mobile Device";
        } else if (userAgent.contains("Tablet")) {
            return "Tablet Device";
        } else {
            return "Desktop Device";
        }
    }

    private String extractIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setDepartment(user.getDepartment());
        response.setAge(user.getAge());
        response.setRole(user.getRole());
        response.setEnabled(user.isEnabled());
        response.setCreatedDate(user.getCreatedDate());
        response.setUpdatedDate(user.getUpdatedDate());
        return response;
    }
}
