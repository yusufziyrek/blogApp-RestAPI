package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

public interface GetAllPostsUseCase {
    PageResponse<PostDomain> execute(int page, int size);
}
