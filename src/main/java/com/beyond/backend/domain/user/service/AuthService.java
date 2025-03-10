package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public interface AuthService {

    CustomUserDetails getCurrentUser();

    boolean isAdminFromUserDetails(CustomUserDetails userDetails);

    void join(JoinRequestDto dto);

    TokenResponseDto login(LoginRequestDto dto);

    void logout(String bearerToken);

    TokenResponseDto refresh(String bearerToken);

    BanResponseDto banUser(BanRequestDto dto);

    UnlockResponseDto unlockUser(UnlockRequestDto dto);

    static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
