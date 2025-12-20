package com.yusufziyrek.blogApp.shared.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusufziyrek.blogApp.shared.exception.CustomProblemDetail;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		log.warn("Unauthorized access attempt to path: {} | IP: {} | Reason: {}",
				request.getRequestURI(),
				request.getRemoteAddr(),
				authException.getMessage());

		CustomProblemDetail problemDetail = new CustomProblemDetail(
				HttpStatus.UNAUTHORIZED.value(),
				ErrorMessages.AUTHENTICATION_REQUIRED,
				request.getRequestURI(),
				getMachineName(),
				LocalDateTime.now());

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
	}

	private String getMachineName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			return "Unknown";
		}
	}
}
