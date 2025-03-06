package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;


public interface AuthService {
    void join(JoinRequestDto dto);

    TokenResponseDto login(LoginRequestDto dto);

    void logout(String bearerToken);

    TokenResponseDto refresh(String bearerToken);

    BanResponseDto banUser(BanRequestDto dto);
}
