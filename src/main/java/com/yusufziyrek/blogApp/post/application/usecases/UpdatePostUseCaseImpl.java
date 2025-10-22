package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.PostException;

public class UpdatePostUseCaseImpl implements UpdatePostUseCase {
    
    private final PostRepository postRepository;
    
    public UpdatePostUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public PostDomain execute(Long postId, String title, String text, Long userId) {
        // Post'u bul
        PostDomain post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorMessages.POST_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        // Kullanıcı bu postu düzenleyebilir mi kontrol et (domain mantığı)
        if (!post.canBeEditedBy(userId)) {
            throw new PostException(ErrorMessages.POST_ACCESS_DENIED_UPDATE, HttpStatus.FORBIDDEN);
        }
        
        // Başlık değişiyorsa, aynı başlık var mı kontrol et
        if (title != null && !title.equals(post.getTitle()) && postRepository.existsByTitleAndIdNot(title, postId)) {
            throw new PostException(ErrorMessages.POST_TITLE_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        
        try {
            // Domain mantığı ile güncelle
            post.updateContent(title, text);
            
            PostDomain updatedPost = postRepository.save(post);
            return updatedPost;
        } catch (IllegalArgumentException e) {
            throw new PostException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
