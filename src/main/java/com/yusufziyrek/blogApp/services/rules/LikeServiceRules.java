package com.yusufziyrek.blogApp.services.rules;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.repo.ILikeRepository;
import com.yusufziyrek.blogApp.shared.exception.LikeException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LikeServiceRules {

	private ILikeRepository likeRepository;

	public void checkIfLikeAlreadyExistForPost(Long userId, Long postId) {
		if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
			throw new LikeException("The post has already been liked!");
		}
	}

	public void checkIfLikeAlreadyExistForComment(Long userId, Long commentId) {
		if (likeRepository.existsByUserIdAndCommentId(userId, commentId)) {
			throw new LikeException("The comment has already been liked!");
		}
	}
}
