package com.yusufziyrek.blogApp.like.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.PostEntity;
import com.yusufziyrek.blogApp.comment.infrastructure.persistence.CommentEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "post_id" }),
        @UniqueConstraint(columnNames = { "user_id", "comment_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // İş mantığı doğrulaması
    public boolean isValid() {
        // Bir beğeni ya posta ya da yoruma referans vermeli, ikisine birden değil
        return (post != null && comment == null) || (post == null && comment != null);
    }

    public boolean isPostLike() {
        return post != null && comment == null;
    }

    public boolean isCommentLike() {
        return comment != null && post == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LikeEntity that))
            return false;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
