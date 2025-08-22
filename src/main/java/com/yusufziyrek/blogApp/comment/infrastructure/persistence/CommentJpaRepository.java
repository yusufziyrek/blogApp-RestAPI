package com.yusufziyrek.blogApp.comment.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    
    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.user LEFT JOIN FETCH c.post WHERE c.post.id = :postId")
    Page<CommentEntity> findByPostIdWithUserAndPost(@Param("postId") Long postId, Pageable pageable);
    
    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.user LEFT JOIN FETCH c.post WHERE c.user.id = :userId")
    Page<CommentEntity> findByUserIdWithUserAndPost(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.post.id = :postId")
    long countByPostId(@Param("postId") Long postId);
    
    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}
