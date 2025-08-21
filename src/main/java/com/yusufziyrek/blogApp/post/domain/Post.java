package com.yusufziyrek.blogApp.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.like.domain.Like;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "posts",
	indexes = {
		@Index(name = "idx_posts_user_id", columnList = "user_id"),
		@Index(name = "idx_posts_created_date", columnList = "createdDate")
	}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 50)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 200)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String text;

	@CreationTimestamp
	private LocalDateTime createdDate;

	@UpdateTimestamp
	private LocalDateTime updatedDate;

	private int commentCount = 0;

	private int likeCount = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Comment> comments;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Like> likes;


	public void incrementCommentCount() {
		this.commentCount++;
	}

	public void decrementCommentCount() {
		this.commentCount--;
	}

	public void incrementLikeCount() {
		this.likeCount++;
	}

	public void decrementLikeCount() {
		this.likeCount--;
	}

	// Domain Methods - Clean Architecture approach
	public void publish() {
		if (this.title == null || this.title.trim().isEmpty()) {
			throw new IllegalArgumentException("Post title cannot be empty");
		}
		if (this.text == null || this.text.trim().isEmpty()) {
			throw new IllegalArgumentException("Post content cannot be empty");
		}
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
	}

	public boolean canBeEditedBy(Long userId) {
		return this.user != null && this.user.getId().equals(userId);
	}

	public void updateContent(String newTitle, String newText) {
		if (newTitle != null && !newTitle.trim().isEmpty()) {
			this.title = newTitle;
		}
		if (newText != null && !newText.trim().isEmpty()) {
			this.text = newText;
		}
		this.updatedDate = LocalDateTime.now();
	}

}
