package com.yusufziyrek.blogApp.post.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.infrastructure.mappers.PostMapper;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.infrastructure.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

/**
 * PostRepository'nin infrastructure implementasyonu
 * Domain ile JPA persist katmanını bağlıyorum
 */
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    
    private final PostJpaRepository jpaRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository; // Entity oluştururken user çekmek için
    
    @Override
    public PostDomain save(PostDomain post) {
    // Post için user bilgisini alıyorum
        UserDomain userDomain = userRepository.findById(post.getUserId())
            .orElseThrow(() -> new RuntimeException("UserDomain not found: " + post.getUserId()));
        
        PostEntity entity = postMapper.toEntity(post, UserMapper.toEntity(userDomain));
        PostEntity savedEntity = jpaRepository.save(entity);
        return postMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<PostDomain> findById(Long id) {
        return jpaRepository.findById(id)
            .map(postMapper::toDomain);
    }
    
    @Override
    public List<PostDomain> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return jpaRepository.findAll(pageable)
            .getContent()
            .stream()
            .map(postMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PostDomain> findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return jpaRepository.findByUserIdWithUser(userId, pageable)
            .getContent()
            .stream()
            .map(postMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public boolean existsByTitle(String title) {
        return jpaRepository.existsByTitle(title);
    }
    
    @Override
    public boolean existsByTitleAndIdNot(String title, Long id) {
        return jpaRepository.existsByTitleAndIdNot(title, id);
    }
    
    @Override
    public long getTotalCount() {
        return jpaRepository.count();
    }
    
    @Override
    public long getTotalCountByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }
}
