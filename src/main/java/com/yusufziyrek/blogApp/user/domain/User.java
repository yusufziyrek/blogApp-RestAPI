package com.yusufziyrek.blogApp.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(
	name = "users",
	indexes = {
		@Index(name = "idx_users_username", columnList = "user_name"),
		@Index(name = "idx_users_email", columnList = "email")
	}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@BatchSize(size = 50)
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", length = 100)
	private String firstname;

	@Column(name = "last_name", length = 100)
	private String lastname;

	@Column(name = "user_name", unique = true, length = 64)
	private String username;

	@Column(unique = true, nullable = false, length = 320)
	private String email;

	@Column(length = 100)
	private String password;

	@Column(length = 100)
	private String department;

	private int age;

	@Enumerated(EnumType.STRING)
	private Role role;

	private boolean enabled = false;

	@Column(name = "created_date", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	@UpdateTimestamp
	private LocalDateTime updatedDate;

	// Domain Methods - Clean Architecture approach
	public void activate() {
		this.enabled = true;
	}

	public void deactivate() {
		this.enabled = false;
	}

	public boolean canUpdateProfile(Long requestUserId) {
		return this.id.equals(requestUserId);
	}

	public void updateProfile(String firstname, String lastname, String email, String department, int age) {
		if (firstname != null && !firstname.trim().isEmpty()) {
			this.firstname = firstname;
		}
		if (lastname != null && !lastname.trim().isEmpty()) {
			this.lastname = lastname;
		}
		if (email != null && !email.trim().isEmpty()) {
			this.email = email;
		}
		if (department != null) {
			this.department = department;
		}
		if (age > 0) {
			this.age = age;
		}
	}

	public String getFullName() {
		return (firstname != null ? firstname : "") + " " + (lastname != null ? lastname : "");
	}

	// UserDetails implementation
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
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

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
