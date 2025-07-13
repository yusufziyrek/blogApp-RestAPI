package com.yusufziyrek.blogApp.identity.service.abstracts;

import com.yusufziyrek.blogApp.identity.dto.requests.LoginRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.AuthResponse;

public interface IAuthService {

	String register(RegisterRequest request);

	AuthResponse login(LoginRequest request);

	String verifyAccount(String token);
}
