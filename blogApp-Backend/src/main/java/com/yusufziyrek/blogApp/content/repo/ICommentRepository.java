package com.yusufziyrek.blogApp.content.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.content.domain.models.Comment;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPostId(Long postId);

	List<Comment> findByUserId(Long userId);

}
