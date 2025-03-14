package com.beyond.backend.domain.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beyond.backend.domain.user.dto.AllUserResponseDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminRequestDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;
import com.beyond.backend.domain.user.dto.OneUserResponseDto;

public interface AdminService {

    DeleteUserByAdminResponseDto deleteUserByAdmin(DeleteUserByAdminRequestDto dto);

    Page<AllUserResponseDto> getUsers(Pageable pageable);

    OneUserResponseDto getOneUser(Long userNo);
}
