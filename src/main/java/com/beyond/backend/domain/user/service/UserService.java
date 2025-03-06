package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.PasswordUpdateRequestDto;
import com.beyond.backend.domain.user.dto.PasswordUpdateResponseDto;
import com.beyond.backend.domain.user.dto.UserUpdateRequestDto;
import com.beyond.backend.domain.user.dto.UserUpdateResponseDto;

public interface UserService {

    public UserUpdateResponseDto updateUser(Long id, UserUpdateRequestDto dto);

    public void deleteUser(String username);

    public PasswordUpdateResponseDto updatePassword(PasswordUpdateRequestDto dto);
}
