package com.yusufziyrek.blogApp.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.entities.Post;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {

	List<Post> findByUserId(Long userId);

}
