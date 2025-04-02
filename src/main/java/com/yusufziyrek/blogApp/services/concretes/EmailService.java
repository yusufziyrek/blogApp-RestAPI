package com.yusufziyrek.blogApp.services.concretes;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;

	public void sendVerificationEmail(String toEmail, String token) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setTo(toEmail);
			helper.setSubject("Hesap Doğrulama");

			String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;

			String htmlContent = """
					<html>
					<head>
					  <meta charset="UTF-8">
					  <title>Hesap Doğrulama</title>
					  <style>
					    .container {
					        font-family: Arial, sans-serif;
					        margin: 20px;
					    }
					    .title {
					        font-size: 18px;
					        color: #4CAF50;
					        font-weight: bold;
					    }
					    .content {
					        margin-top: 15px;
					        line-height: 1.6;
					    }
					    .button {
					        display: inline-block;
					        margin-top: 20px;
					        padding: 10px 15px;
					        text-decoration: none;
					        background-color: #4CAF50;
					        color: #fff;
					        border-radius: 4px;
					    }
					  </style>
					</head>
					<body>
					  <div class="container">
					    <div class="title">Hesabınızı Doğrulayın</div>
					    <div class="content">
					      <p>Merhaba,</p>
					      <p>Hesabınızı aktif etmek için aşağıdaki butona tıklayabilirsiniz:</p>
					      <a class="button" href="%s">Hesabımı Doğrula</a>
					      <p>Eğer linke tıklayamıyorsanız, aşağıdaki adresi tarayıcınıza kopyalayın:</p>
					      <p>%s</p>
					    </div>
					  </div>
					</body>
					</html>
					""".formatted(verificationLink, verificationLink);

			helper.setText(htmlContent, true);

			mailSender.send(mimeMessage);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
