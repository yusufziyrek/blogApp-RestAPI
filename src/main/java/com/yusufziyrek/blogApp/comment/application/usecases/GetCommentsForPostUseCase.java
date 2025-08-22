package com.yusufziyrek.blogApp.comment.application.usecases;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

public interface GetCommentsForPostUseCase {
    PageResponse<CommentDomain> execute(Long postId, int page, int size);
}
