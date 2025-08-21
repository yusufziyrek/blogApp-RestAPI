package com.yusufziyrek.blogApp.comment.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.post.domain.Post;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "comments",
	indexes = {
		@Index(name = "idx_comments_post_id", columnList = "post_id"),
		@Index(name = "idx_comments_user_id", columnList = "user_id"),
		@Index(name = "idx_comments_created_date", columnList = "createdDate")
	}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 50)
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String text;

	private int likeCount = 0;

	@CreationTimestamp
	private LocalDateTime createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;

	public void incrementLikeCount() {
		this.likeCount++;
	}

	public void decrementLikeCount() {
		this.likeCount--;
	}

	// Domain Methods - Clean Architecture approach
	public void publish() {
		if (this.text == null || this.text.trim().isEmpty()) {
			throw new IllegalArgumentException("Comment text cannot be empty");
		}
		this.createdDate = LocalDateTime.now();
	}

	public boolean canBeEditedBy(Long userId) {
		return this.user != null && this.user.getId().equals(userId);
	}

	public void updateText(String newText) {
		if (newText != null && !newText.trim().isEmpty()) {
			this.text = newText;
		}
	}
}
