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

@Entity
@Table(name = "likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = true)
	@JsonIgnore
	private Post post;

	@ManyToOne
	@JoinColumn(name = "comment_id", nullable = true)
	@JsonIgnore
	private Comment comment;

	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;

	@PrePersist
	protected void onCreate() {
		createdDate = LocalDateTime.now();
	}

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
