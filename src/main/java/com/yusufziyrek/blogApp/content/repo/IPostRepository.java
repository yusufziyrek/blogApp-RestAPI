package com.yusufziyrek.blogApp.content.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.content.domain.Post;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAll(Pageable pageable);

	Page<Post> findAllByUserId(Pageable pageable, Long userId);

	List<Post> findByUserId(Long userId);
	
	Page<Post> findByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(String title, String text, Pageable pageable);

}
