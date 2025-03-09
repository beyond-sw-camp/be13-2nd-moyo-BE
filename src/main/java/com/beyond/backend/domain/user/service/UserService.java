package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.PasswordUpdateRequestDto;
import com.beyond.backend.domain.user.dto.PasswordUpdateResponseDto;
import com.beyond.backend.domain.user.dto.UserUpdateRequestDto;
import com.beyond.backend.domain.user.dto.UserUpdateResponseDto;

public interface UserService {

    UserUpdateResponseDto updateUser(Long id, UserUpdateRequestDto dto);

    void deleteUser(String username);

    PasswordUpdateResponseDto updatePassword(PasswordUpdateRequestDto dto);
}
