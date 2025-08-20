package com.yusufziyrek.blogApp.identity.service.concretes;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.identity.service.abstracts.IEmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

	public void sendVerificationEmail(String toEmail, String token) {
		// Email işlemleri geçici olarak askıya alındı
		log.info("Email gönderme işlemi devre dışı - Email: {} için doğrulama kodu oluşturuldu", toEmail);
		log.info("Doğrulama linki: http://localhost:8080/api/v1/auth/verify?token={}", token);
	}
}
