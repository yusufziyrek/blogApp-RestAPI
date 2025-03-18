package com.yusufziyrek.blogApp.services.concretes;

import com.yusufziyrek.blogApp.entities.User;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final IUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
		return user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
	}
}
