package com.yusufziyrek.blogApp.content.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.content.domain.models.Post;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAll(Pageable pageable);

	Page<Post> findAllByUserId(Pageable pageable, Long userId);

	List<Post> findByUserId(Long userId);
	
	Page<Post> findByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(String title, String text, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.id = :id")
	Optional<Post> findByIdWithUser(@Param("id") Long id);

	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.user WHERE p.id = :id")
	Optional<Post> findByIdWithUserFetch(@Param("id") Long id);

	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :id")
	Optional<Post> findByIdWithComments(@Param("id") Long id);

	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes WHERE p.id = :id")
	Optional<Post> findByIdWithLikes(@Param("id") Long id);

	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.comments LEFT JOIN FETCH p.likes WHERE p.id = :id")
	Optional<Post> findByIdWithAllRelations(@Param("id") Long id);

	@Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
	Long countByUserId(@Param("userId") Long userId);

	@Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createdDate DESC")
	List<Post> findByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);

	@Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.text LIKE %:keyword% ORDER BY p.createdDate DESC")
	Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

}
