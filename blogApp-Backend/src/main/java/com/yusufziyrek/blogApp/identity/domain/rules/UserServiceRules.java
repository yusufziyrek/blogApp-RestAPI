package com.yusufziyrek.blogApp.identity.domain.rules;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceRules {

	private IUserRepository userRepository;

	public void checkIfUserNameExists(String userName) {
		if (this.userRepository.existsByUsername(userName)) {
			throw new UserException(String.format(ErrorMessages.USERNAME_ALREADY_EXISTS, userName));
		}
	}

}
