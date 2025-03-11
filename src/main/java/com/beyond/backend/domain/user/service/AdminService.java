package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.DeleteUserByAdminRequestDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;

public interface AdminService {

    DeleteUserByAdminResponseDto DeleteUserByAdmin(DeleteUserByAdminRequestDto dto);

}