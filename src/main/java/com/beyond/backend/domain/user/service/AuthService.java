package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;


public interface AuthService {

    CustomUserDetails getCurrentUser();

    boolean isAdminFromUserDetails(CustomUserDetails userDetails);

    void join(JoinRequestDto dto);

    TokenResponseDto login(LoginRequestDto dto);

    void logout(String bearerToken);

    TokenResponseDto refresh(String bearerToken);

    

    
}
