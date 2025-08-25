package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.post.dto.request.CreatePostRequest;
import com.yusufziyrek.blogApp.shared.exception.PostException;

public class CreatePostUseCaseImpl implements CreatePostUseCase {
    
    private final PostRepository postRepository;
    
    public CreatePostUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public PostDomain execute(CreatePostRequest request, Long userId) {
        // Aynı başlık var mı kontrol et
        if (postRepository.existsByTitle(request.getTitle())) {
            throw new PostException("A post with this title already exists");
        }
        
        try {
            // Domain nesnesini oluştur
            PostDomain post = new PostDomain(
                request.getTitle(),
                request.getText(),
                userId
            );
            
            // Post'u kaydet
            PostDomain savedPost = postRepository.save(post);
            
            return savedPost;
                
        } catch (IllegalArgumentException e) {
            throw new PostException(e.getMessage());
        }
    }
}
