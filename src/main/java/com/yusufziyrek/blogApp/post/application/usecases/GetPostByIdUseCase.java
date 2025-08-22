package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.domain.PostDomain;

public interface GetPostByIdUseCase {
    PostDomain execute(Long postId);
}
