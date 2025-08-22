package com.yusufziyrek.blogApp.auth.application.ports.usecases;

public interface RevokeRefreshTokenUseCase {
    void execute(String tokenValue);
    void executeByUser(String email);
}
