package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.AllUserResponseDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;
import com.beyond.backend.domain.user.dto.OneUserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    DeleteUserByAdminResponseDto delete(Long userNo);

    Page<AllUserResponseDto> getUsers(Pageable pageable);

    OneUserResponseDto getOneUser(Long userNo);
}
