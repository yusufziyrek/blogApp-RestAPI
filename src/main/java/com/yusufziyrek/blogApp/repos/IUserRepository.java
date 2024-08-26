package com.yusufziyrek.blogApp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	boolean existsByUserName(String userName);

}
