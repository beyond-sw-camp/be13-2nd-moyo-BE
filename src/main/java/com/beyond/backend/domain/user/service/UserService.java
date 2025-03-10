package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;

public interface UserService {

    UserUpdateResponseDto updateUser(Long id, UserUpdateRequestDto dto);

    void deleteUser(String username);

    PasswordUpdateResponseDto updatePassword(PasswordUpdateRequestDto dto);

    BanResponseDto banUser(BanRequestDto dto);

    UnlockResponseDto unlockUser(UnlockRequestDto dto);

}
