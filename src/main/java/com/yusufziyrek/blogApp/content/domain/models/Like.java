package com.yusufziyrek.blogApp.content.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.identity.domain.models.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "likes", indexes = { 
		@Index(name = "idx_likes_user_id", columnList = "user_id"),
		@Index(name = "idx_likes_post_id", columnList = "post_id"),
		@Index(name = "idx_likes_comment_id", columnList = "comment_id"),
		@Index(name = "idx_likes_user_post", columnList = "user_id, post_id"),
		@Index(name = "idx_likes_user_comment", columnList = "user_id, comment_id") },
		uniqueConstraints = {
			@UniqueConstraint(name = "uk_likes_user_post", columnNames = {"user_id", "post_id"}),
			@UniqueConstraint(name = "uk_likes_user_comment", columnNames = {"user_id", "comment_id"})
		})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = true)
	@JsonIgnore
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id", nullable = true)
	@JsonIgnore
	private Comment comment;

	@Override
	public String toString() {
		return "Like{" +
				"id=" + id +
				", userId=" + (user != null ? user.getId() : null) +
				", postId=" + (post != null ? post.getId() : null) +
				", commentId=" + (comment != null ? comment.getId() : null) +
				'}';
	}
}