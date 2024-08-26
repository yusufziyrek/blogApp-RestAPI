package com.yusufziyrek.blogApp.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.entities.Comment;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPostId(Long postId);

	List<Comment> findByUserId(Long userId);

}
