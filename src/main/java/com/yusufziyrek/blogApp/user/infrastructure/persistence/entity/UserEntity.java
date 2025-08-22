package com.yusufziyrek.blogApp.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * JPA Entity for UserDomain - Infrastructure Layer
 * Contains framework-specific annotations and implementations
 */
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_username", columnList = "user_name"),
        @Index(name = "idx_users_email", columnList = "email")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@BatchSize(size = 50)
public class UserEntity {

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
    private RoleEntity role;

    private boolean enabled = true;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
