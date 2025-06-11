package com.yusufziyrek.blogApp.content.domain.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.identity.domain.models.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts", indexes = { @Index(name = "idx_posts_user_id", columnList = "user_id") })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String text;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private int commentCount = 0;

	private int likeCount = 0;

	@ManyToOne
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

}
