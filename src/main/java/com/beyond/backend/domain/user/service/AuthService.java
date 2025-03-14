package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.User;


public interface AuthService {

    CustomUserDetails getCurrentUser();

    void validateUser(User user);

    void validateAdminAuthorization();

    boolean isAdmin();

    void join(JoinRequestDto dto);

    TokenResponseDto login(LoginRequestDto dto);

    void logout(String bearerToken);

    TokenResponseDto refresh(String bearerToken);

    

    
}
