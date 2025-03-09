package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public interface AuthService {
    void join(JoinRequestDto dto);

    TokenResponseDto login(LoginRequestDto dto);

    void logout(String bearerToken);

    TokenResponseDto refresh(String bearerToken);

    BanResponseDto banUser(BanRequestDto dto);

    UnlockResponseDto unlockUser(UnlockRequestDto dto);

    CustomUserDetails getCurrentUser();

    boolean isAdminFromUserDetails(CustomUserDetails userDetails);
}
