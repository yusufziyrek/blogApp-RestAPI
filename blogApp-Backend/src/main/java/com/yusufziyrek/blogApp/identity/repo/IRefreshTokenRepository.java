package com.yusufziyrek.blogApp.identity.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.identity.domain.models.RefreshToken;
import com.yusufziyrek.blogApp.identity.domain.models.User;

import java.util.Optional;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	int deleteByUser(User user);
}
