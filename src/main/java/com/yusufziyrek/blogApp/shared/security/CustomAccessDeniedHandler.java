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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {

		log.warn("Access denied for path: {} | IP: {} | Reason: {}",
				request.getRequestURI(),
				request.getRemoteAddr(),
				accessDeniedException.getMessage());

		CustomProblemDetail problemDetail = new CustomProblemDetail(
				HttpStatus.FORBIDDEN.value(),
				ErrorMessages.ACCESS_DENIED,
				request.getRequestURI(),
				getMachineName(),
				LocalDateTime.now());

		response.setStatus(HttpStatus.FORBIDDEN.value());
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
