package com.yusufziyrek.blogApp.identity.service.abstracts;

public interface IEmailService {
	
	public void sendVerificationEmail(String toEmail, String token);

}
