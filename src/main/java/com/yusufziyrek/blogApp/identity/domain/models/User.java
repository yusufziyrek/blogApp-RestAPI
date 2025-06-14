package com.yusufziyrek.blogApp.identity.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yusufziyrek.blogApp.content.domain.models.Comment;
import com.yusufziyrek.blogApp.content.domain.models.Like;
import com.yusufziyrek.blogApp.content.domain.models.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", indexes = { 
		@Index(name = "idx_users_user_name", columnList = "user_name"),
		@Index(name = "idx_users_email", columnList = "email"),
		@Index(name = "idx_users_first_name", columnList = "first_name"),
		@Index(name = "idx_users_last_name", columnList = "last_name") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name")
	private String firstname;

	@Column(name = "last_name")
	private String lastname;

	@Column(name = "user_name", unique = true, nullable = false)
	private String username;

	@Column(unique = true, nullable = false)
	private String email;

	private String password;

	private String department;

	private Integer age;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Post> posts;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Comment> comments;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Like> likes;

	@Enumerated(EnumType.STRING)
	private Role role;

	private boolean enabled = false;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}