package com.yusufziyrek.blogApp.like.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.comment.domain.Comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
	name = "likes",
	indexes = {
		@Index(name = "idx_likes_user_id", columnList = "user_id"),
		@Index(name = "idx_likes_post_id", columnList = "post_id"),
		@Index(name = "idx_likes_comment_id", columnList = "comment_id")
	},
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_like_user_post", columnNames = {"user_id", "post_id"}),
		@UniqueConstraint(name = "uk_like_user_comment", columnNames = {"user_id", "comment_id"})
	}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 50)
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = true)
	@JsonIgnore
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id", nullable = true)
	@JsonIgnore
	private Comment comment;

	@Column(name = "created_date", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;

	@AssertTrue(message = "Either post or comment must be specified")
	public boolean isPostOrCommentSpecified() {
		return (post != null && comment == null) || (post == null && comment != null);
	}

	// Domain Methods - Clean Architecture approach
	public boolean isForPost() {
		return post != null;
	}

	public boolean isForComment() {
		return comment != null;
	}

	public boolean canBeRemovedBy(Long userId) {
		return this.user != null && this.user.getId().equals(userId);
	}
}
