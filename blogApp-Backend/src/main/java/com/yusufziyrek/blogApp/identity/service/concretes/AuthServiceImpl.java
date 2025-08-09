package com.yusufziyrek.blogApp.identity.service.concretes;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.identity.domain.models.RefreshToken;
import com.yusufziyrek.blogApp.identity.domain.models.Role;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.identity.dto.requests.LoginRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.AuthResponse;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import com.yusufziyrek.blogApp.identity.repo.IVerificationTokenRepository;
import com.yusufziyrek.blogApp.identity.service.abstracts.IAuthService;
import com.yusufziyrek.blogApp.shared.exception.AuthException;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final IVerificationTokenRepository verificationTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(String.format(ErrorMessages.EMAIL_ALREADY_EXISTS, request.getEmail()));
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthException(String.format(ErrorMessages.USERNAME_ALREADY_EXISTS, request.getUsername()));
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setDepartment(request.getDepartment());
        user.setAge(request.getAge());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        // Email aktivasyonunu geçici olarak devre dışı bırakıyoruz
        user.setEnabled(true);
        userRepository.save(user);

        // Email doğrulama token'ı oluşturmayı geçici olarak devre dışı bırakıyoruz
        // String token = UUID.randomUUID().toString();
        // VerificationToken verificationToken = new VerificationToken();
        // verificationToken.setToken(token);
        // verificationToken.setUser(user);
        // verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        // verificationTokenRepository.save(verificationToken);

        return "User registered successfully! You can now log in.";
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailOrUsername(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new AuthException(String.format(ErrorMessages.USER_NOT_FOUND_BY_EMAIL, request.getUsernameOrEmail())));
        // Email doğrulama kontrolünü geçici olarak devre dışı bırakıyoruz
        // if (!user.isEnabled()) {
        //     throw new AuthException(ErrorMessages.USER_ACCOUNT_NOT_VERIFIED);
        // }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));
        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return new AuthResponse(accessToken, refreshToken.getToken(), "Login successful!", user);
    }

    public String verifyAccount(String token) {
        var verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(ErrorMessages.INVALID_TOKEN));
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException(ErrorMessages.TOKEN_EXPIRED);
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "Account verified successfully. You can now log in!";
    }
}
