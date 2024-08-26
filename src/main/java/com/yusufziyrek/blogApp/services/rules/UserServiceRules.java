package com.yusufziyrek.blogApp.services.rules;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.utilites.exceptions.UserException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceRules {

	private IUserRepository userRepository;

	public void checkIfUserNameExists(String userName) {
		if (this.userRepository.existsByUserName(userName)) {
			throw new UserException("Username already exists !");

		}

	}

}
