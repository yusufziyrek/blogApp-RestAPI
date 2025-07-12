package com.yusufziyrek.blogApp.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	private static final List<String> EXCLUDED_PATHS = Arrays.asList(
		"/api/v1/auth/refresh",
		"/api/v1/auth/login",
		"/api/v1/auth/register",
		"/api/v1/auth/verify",
		"/actuator/health",
		"/actuator/info"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		
		// Skip authentication for excluded paths
		if (isExcludedPath(requestURI)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = extractTokenFromRequest(request);
		
		if (!StringUtils.hasText(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String email = jwtUtil.extractEmail(token);
			if (StringUtils.hasText(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				if (jwtUtil.validateToken(token)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					log.debug("Authentication successful for user: {}", email);
				} else {
					log.warn("Invalid JWT token for user: {}", email);
				}
			}
		} catch (Exception ex) {
			log.warn("JWT authentication failed: {}", ex.getMessage());
			if (HttpMethod.GET.matches(request.getMethod())) {
				SecurityContextHolder.clearContext();
			} else {
				throw ex;
			}
		}

		filterChain.doFilter(request, response);
	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	private boolean isExcludedPath(String requestURI) {
		return EXCLUDED_PATHS.stream().anyMatch(requestURI::startsWith);
	}
}
