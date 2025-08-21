package com.yusufziyrek.blogApp.comment.infrastructure.persistence;

import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    
    private final JpaCommentRepository jpaCommentRepository;
    
    @Override
    public Comment save(Comment comment) {
        return jpaCommentRepository.save(comment);
    }
    
    @Override
    public Optional<Comment> findById(Long id) {
        return jpaCommentRepository.findById(id);
    }
    
    @Override
    public List<Comment> findByPostId(Long postId) {
        return jpaCommentRepository.findByPostId(postId);
    }
    
    @Override
    public Page<Comment> findByPostId(Long postId, Pageable pageable) {
        return jpaCommentRepository.findByPostId(postId, pageable);
    }
    
    @Override
    public Page<Comment> findByUserId(Long userId, Pageable pageable) {
        return jpaCommentRepository.findByUserId(userId, pageable);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaCommentRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaCommentRepository.existsById(id);
    }
    
    @Override
    public long countByPostId(Long postId) {
        return jpaCommentRepository.countByPostId(postId);
    }
}
