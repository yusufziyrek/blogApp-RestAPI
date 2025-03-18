package com.yusufziyrek.blogApp.repos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	Page<User> findAll(Pageable pageable);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmailOrUsername(String email, String username);

	boolean existsByEmail(String email);

	boolean existsByUsername(String userName);

}
