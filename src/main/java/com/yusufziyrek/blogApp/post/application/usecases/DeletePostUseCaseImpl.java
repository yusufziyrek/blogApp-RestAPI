package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.AuthException;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.PostException;

public class DeletePostUseCaseImpl implements DeletePostUseCase {
    
    private final PostRepository postRepository;
    
    public DeletePostUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public void execute(Long postId, Long currentUserId) {
        // Önce post var mı kontrol et
        PostDomain post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, postId), HttpStatus.NOT_FOUND));
        
        // Sahiplik kontrolü - sadece yazan silebilir
        if (!post.getUserId().equals(currentUserId)) {
            throw new AuthException(ErrorMessages.POST_ACCESS_DENIED_DELETE, HttpStatus.FORBIDDEN);
        }
        
        // Post'u sil
        postRepository.deleteById(postId);
    }
}
