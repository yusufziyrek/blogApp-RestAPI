package com.yusufziyrek.blogApp.services.abstracts;

public interface IEmailService {
	
	public void sendVerificationEmail(String toEmail, String token);

}
