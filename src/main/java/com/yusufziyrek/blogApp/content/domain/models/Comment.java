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
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

	@OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Like> likes;

	public void incrementLikeCount() {
		this.likeCount++;

	}

	public void decrementLikeCount() {
		this.likeCount--;

	}
}
