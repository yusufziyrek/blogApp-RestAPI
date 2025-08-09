package com.yusufziyrek.blogApp.content.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.identity.domain.models.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@AssertTrue(message = "Either post or comment must be specified")
	public boolean isPostOrCommentSpecified() {
		return (post != null && comment == null) || (post == null && comment != null);
	}

}
