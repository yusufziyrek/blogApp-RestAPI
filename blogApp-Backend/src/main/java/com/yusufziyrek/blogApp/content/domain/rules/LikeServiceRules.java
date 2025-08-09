package com.yusufziyrek.blogApp.content.domain.rules;

import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.repo.ILikeRepository;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.LikeException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LikeServiceRules {

	private ILikeRepository likeRepository;

	public void checkIfLikeAlreadyExistForPost(Long userId, Long postId) {
		if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
			throw new LikeException(ErrorMessages.LIKE_ALREADY_EXISTS_FOR_POST);
		}
	}

	public void checkIfLikeAlreadyExistForComment(Long userId, Long commentId) {
		if (likeRepository.existsByUserIdAndCommentId(userId, commentId)) {
			throw new LikeException(ErrorMessages.LIKE_ALREADY_EXISTS_FOR_COMMENT);
		}
	}

	public void validateLikeData(Long postId, Long commentId) {
		if ((postId == null && commentId == null) || (postId != null && commentId != null)) {
			throw new LikeException("Like must be associated with either a post or a comment, not both or neither");
		}
	}
}
