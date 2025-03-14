package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;

public interface UserService {

    UserUpdateResponseDto updateUser(Long userNo, UserUpdateRequestDto dto);

    void deleteUser(String username);

    PasswordUpdateResponseDto updatePassword(String username, PasswordUpdateRequestDto dto);

    BanResponseDto banUser(BanRequestDto dto);

    UnlockResponseDto unlockUser(UnlockRequestDto dto);
}
