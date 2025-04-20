package com.yusufziyrek.blogApp.services.rules;

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

}
