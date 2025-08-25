package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.AuthException;

public class DeletePostUseCaseImpl implements DeletePostUseCase {
    
    private final PostRepository postRepository;
    
    public DeletePostUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public void execute(Long postId, Long currentUserId) {
    // Önce post var mı kontrol et
        PostDomain post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        
    // Sahiplik kontrolü - sadece yazan silebilir
        if (!post.getUserId().equals(currentUserId)) {
            throw new AuthException("You can only delete your own posts");
        }
        
    // Post'u sil
        postRepository.deleteById(postId);
    }
}
