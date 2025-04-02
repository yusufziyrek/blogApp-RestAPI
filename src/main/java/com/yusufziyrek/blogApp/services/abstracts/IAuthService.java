package com.yusufziyrek.blogApp.services.abstracts;

import com.yusufziyrek.blogApp.services.requests.LoginRequest;
import com.yusufziyrek.blogApp.services.requests.RegisterRequest;
import com.yusufziyrek.blogApp.services.responses.AuthResponse;

public interface IAuthService {

	String register(RegisterRequest request);

	AuthResponse login(LoginRequest request);

	String verifyAccount(String token);
}
