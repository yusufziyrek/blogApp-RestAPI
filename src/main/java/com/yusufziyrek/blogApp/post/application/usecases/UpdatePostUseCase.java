package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.domain.PostDomain;

public interface UpdatePostUseCase {
    PostDomain execute(Long postId, String title, String text, Long userId);
}
