package com.yusufziyrek.blogApp.content.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.identity.domain.models.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "likes", indexes = { 
		@Index(name = "idx_likes_user_id", columnList = "user_id"),
		@Index(name = "idx_likes_post_id", columnList = "post_id"),
		@Index(name = "idx_likes_comment_id", columnList = "comment_id") })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = true)
	@JsonIgnore
	private Post post;

	@ManyToOne
	@JoinColumn(name = "comment_id", nullable = true)
	@JsonIgnore
	private Comment comment;

}