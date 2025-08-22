package com.yusufziyrek.blogApp.post.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT p FROM PostEntity p JOIN FETCH p.user")
    Page<PostEntity> findAllWithUsers(Pageable pageable);
    
    @Query("SELECT p FROM PostEntity p JOIN FETCH p.user WHERE p.id = :id")
    Optional<PostEntity> findByIdWithUser(Long id);
    
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, Long id);
}
