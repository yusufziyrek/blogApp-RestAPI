package com.yusufziyrek.blogApp.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String token = extractToken(request);

			if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				// 1. Token'ı parse et ve claimleri al
				Claims claims = jwtUtil.extractAllClaims(token);

				String email = claims.getSubject();
				Long userId = claims.get("id", Long.class);

				// 2. Rolleri token'dan al
				@SuppressWarnings("unchecked")
				List<String> roles = (List<String>) claims.get("roles");

				var authorities = (roles != null)
						? roles.stream().map(SimpleGrantedAuthority::new).toList()
						: List.<SimpleGrantedAuthority>of();

				// 3. Kullanıcı nesnesini oluştur (Password ve Role null geçilebilir, çünkü
				// yetkiler token'dan geldi)
				// Username olarak email'i verdik ki loglarda null görünmesin.
				UserDetails userDetails = new UserPrincipal(userId, email, email, null, null, true);

				// 4. Context'e ata
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, authorities);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (JwtException ex) {
			log.warn("JWT token geçersiz veya süresi dolmuş: {}", ex.getMessage());
			SecurityContextHolder.clearContext();
		} catch (Exception ex) {
			log.error("Authentication hatası", ex);
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
}
