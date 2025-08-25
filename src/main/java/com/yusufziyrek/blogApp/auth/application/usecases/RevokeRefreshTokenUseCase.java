package com.yusufziyrek.blogApp.auth.application.usecases;

public interface RevokeRefreshTokenUseCase {
    void execute(String tokenValue);
    void executeByUser(String email);
}
