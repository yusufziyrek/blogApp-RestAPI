package com.yusufziyrek.blogApp.auth.application.ports.usecases;

import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;

public interface ValidateRefreshTokenUseCase {
    RefreshTokenDomain execute(String tokenValue);
}
