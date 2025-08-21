package com.yusufziyrek.blogApp.comment.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.post.domain.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String text;

	private int likeCount = 0;

	private LocalDateTime createdDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;

	@PrePersist
	protected void onCreate() {
		if (createdDate == null) {
			createdDate = LocalDateTime.now();
		}
	}

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
