package com.yusufziyrek.blogApp.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.entities.Like;

@Repository
public interface ILikeRepository extends JpaRepository<Like, Long> {

	List<Like> findByPostId(Long postId);

	List<Like> findByCommentId(Long commentId);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	boolean existsByUserIdAndCommentId(Long userId, Long commentId);

}
