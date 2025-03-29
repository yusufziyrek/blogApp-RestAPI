package com.yusufziyrek.blogApp.services.concretes;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.entities.Role;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.entities.VerificationToken;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.repos.IVerificationTokenRepository;
import com.yusufziyrek.blogApp.security.JwtUtil;
import com.yusufziyrek.blogApp.services.requests.LoginRequest;
import com.yusufziyrek.blogApp.services.requests.RegisterRequest;
import com.yusufziyrek.blogApp.services.responses.AuthResponse;
import com.yusufziyrek.blogApp.utilites.exceptions.AuthException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final IUserRepository userRepository;
	private final IVerificationTokenRepository verificationTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;

	public String register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())
				|| userRepository.existsByUsername(request.getUsername())) {
			throw new AuthException("Email or Username already in use!");
		}

		User user = new User();
		user.setEmail(request.getEmail());
		user.setUsername(request.getUsername());
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setDepartmant(request.getDepartmant());
		user.setAge(request.getAge());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		user.setEnabled(false);
		userRepository.save(user);

		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
		verificationTokenRepository.save(verificationToken);

		emailService.sendVerificationEmail(user.getEmail(), token);

		return "User registered successfully! Please verify your email to activate your account.";
	}

	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmailOrUsername(request.getUsernameOrEmail(), request.getUsernameOrEmail())
				.orElseThrow(() -> new AuthException("User not found!"));

		if (!user.isEnabled()) {
			throw new AuthException(
					"Your account is not verified yet. Please check your email for the activation link.");
		}

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));

		String token = jwtUtil.generateToken(user.getEmail());
		return new AuthResponse(token, "Login successful!");
	}

	public String verifyAccount(String token) {
		var verificationToken = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new AuthException("Invalid or non-existing token!"));

		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new AuthException("Token expired!");
		}

		User user = verificationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);

		verificationTokenRepository.delete(verificationToken);

		return "Account verified successfully. You can now log in!";
	}
}
