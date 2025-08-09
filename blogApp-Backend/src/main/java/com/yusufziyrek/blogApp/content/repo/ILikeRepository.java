package com.yusufziyrek.blogApp.content.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.content.domain.models.Like;

@Repository
public interface ILikeRepository extends JpaRepository<Like, Long> {

	List<Like> findByPostId(Long postId);

	List<Like> findByCommentId(Long commentId);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	boolean existsByUserIdAndCommentId(Long userId, Long commentId);

	Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

}
