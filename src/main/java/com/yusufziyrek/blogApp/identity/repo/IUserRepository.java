package com.yusufziyrek.blogApp.identity.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.identity.domain.models.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	Page<User> findAll(Pageable pageable);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmailOrUsername(String email, String username);

	boolean existsByEmail(String email);

	boolean existsByUsername(String userName);
	
	Page<User> findByUsernameContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(
	            String username, String firstname, String lastname, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.enabled = true")
	Page<User> findAllEnabled(Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.role = :role")
	Page<User> findByRole(@Param("role") com.yusufziyrek.blogApp.identity.domain.models.Role role, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.enabled = true AND u.role = :role")
	Page<User> findEnabledByRole(@Param("role") com.yusufziyrek.blogApp.identity.domain.models.Role role, Pageable pageable);

	@Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
	Long countEnabledUsers();

	@Query("SELECT u FROM User u WHERE u.department = :department")
	List<User> findByDepartment(@Param("department") String department);

	@Query("SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge")
	Page<User> findByAgeBetween(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.createdAt >= :startDate")
	Page<User> findByCreatedAtAfter(@Param("startDate") java.time.LocalDateTime startDate, Pageable pageable);

}
