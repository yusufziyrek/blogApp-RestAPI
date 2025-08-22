package com.yusufziyrek.blogApp.auth.application.ports.usecases;

import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

public interface CreateRefreshTokenUseCase {
    RefreshTokenDomain execute(UserDomain user, String deviceInfo, String ipAddress);
}
