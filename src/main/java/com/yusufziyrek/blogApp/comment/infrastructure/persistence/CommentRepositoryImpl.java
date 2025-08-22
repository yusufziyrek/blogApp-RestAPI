package com.yusufziyrek.blogApp.comment.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.infrastructure.mappers.CommentMapper;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.PostEntity;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;

import lombok.RequiredArgsConstructor;

/**
 * Infrastructure implementation of CommentRepository port
 * Bridges domain layer with JPA persistence
 */
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    
    private final CommentJpaRepository jpaRepository;
    private final CommentMapper commentMapper;
    
    @Override
    public CommentDomain save(CommentDomain comment) {
    // Güncelleme ise mevcut entity'yi bul, yoksa yeni oluştur
        CommentEntity entity;
        if (comment.getId() != null) {
            entity = jpaRepository.findById(comment.getId()).orElse(new CommentEntity());
        } else {
            entity = new CommentEntity();
        }
        
    // Entity alanlarını güncelle
        entity.setId(comment.getId());
        entity.setText(comment.getText());
        entity.setCreatedDate(comment.getCreatedDate());
        entity.setUpdatedDate(comment.getUpdatedDate());
        
    // İlgili entity'leri minimal referanslarla setle
        if (entity.getPost() == null || !entity.getPost().getId().equals(comment.getPostId())) {
            PostEntity postEntity = new PostEntity();
            postEntity.setId(comment.getPostId());
            entity.setPost(postEntity);
        }
        
        if (entity.getUser() == null || !entity.getUser().getId().equals(comment.getUserId())) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(comment.getUserId());
            entity.setUser(userEntity);
        }
        
        CommentEntity savedEntity = jpaRepository.save(entity);
        return commentMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<CommentDomain> findById(Long id) {
        return jpaRepository.findById(id)
            .map(commentMapper::toDomain);
    }
    
    @Override
    public List<CommentDomain> findByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").ascending());
        return jpaRepository.findByPostIdWithUserAndPost(postId, pageable)
            .getContent()
            .stream()
            .map(commentMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CommentDomain> findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return jpaRepository.findByUserIdWithUserAndPost(userId, pageable)
            .getContent()
            .stream()
            .map(commentMapper::toDomain)
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
    public long getTotalCountByPostId(Long postId) {
        return jpaRepository.countByPostId(postId);
    }
    
    @Override
    public long getTotalCountByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }
    
    @Override
    public long getTotalCount() {
        return jpaRepository.count();
    }
}
