package com.yusufziyrek.blogApp.identity.domain.rules;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceRules {

	private IUserRepository userRepository;

	public void checkIfUserNameExists(String userName) {
		if (this.userRepository.existsByUsername(userName)) {
			throw new UserException("Username already exists !");
		}
	}

	public void checkIfUserExists(String username, String email) {
		if (this.userRepository.existsByUsername(username)) {
			throw new UserException("Username already exists !");
		}
		if (this.userRepository.existsByEmail(email)) {
			throw new UserException("Email already exists !");
		}
	}
}
