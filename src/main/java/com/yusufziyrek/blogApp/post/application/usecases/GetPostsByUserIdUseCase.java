package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

public interface GetPostsByUserIdUseCase {
    PageResponse<PostDomain> execute(Long userId, int page, int size);
}
