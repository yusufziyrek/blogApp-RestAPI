package com.yusufziyrek.blogApp.post.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
    
    @Query("SELECT p FROM PostEntity p LEFT JOIN FETCH p.user WHERE p.id = :id")
    PostEntity findByIdWithUser(@Param("id") Long id);
    
    @Query("SELECT p FROM PostEntity p LEFT JOIN FETCH p.user")
    Page<PostEntity> findAllWithUsers(Pageable pageable);
    
    @Query("SELECT p FROM PostEntity p LEFT JOIN FETCH p.user WHERE p.user.id = :userId")
    Page<PostEntity> findByUserIdWithUser(@Param("userId") Long userId, Pageable pageable);
    
    boolean existsByTitle(String title);
    
    boolean existsByTitleAndIdNot(String title, Long id);
    
    @Query("SELECT COUNT(p) FROM PostEntity p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}
