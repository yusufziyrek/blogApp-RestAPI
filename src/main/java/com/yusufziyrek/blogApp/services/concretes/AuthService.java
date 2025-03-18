package com.yusufziyrek.blogApp.services.concretes;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.entities.Role;
import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.security.JwtUtil;
import com.yusufziyrek.blogApp.services.requests.LoginRequest;
import com.yusufziyrek.blogApp.services.requests.RegisterRequest;
import com.yusufziyrek.blogApp.services.responses.AuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final IUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	public String register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())
				|| userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("Email or Username already in use!");
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

		userRepository.save(user);
		return "User registered successfully!";
	}

	public AuthResponse login(LoginRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));
		User user = userRepository.findByEmailOrUsername(request.getUsernameOrEmail(), request.getUsernameOrEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		String token = jwtUtil.generateToken(user.getEmail());
		return new AuthResponse(token, "Login successful!");
	}
}
