package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.user.dto.AllUserResponseDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;
import com.beyond.backend.domain.user.dto.OneUserResponseDto;
import com.beyond.backend.domain.user.entity.UserSortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    DeleteUserByAdminResponseDto delete(Long userNo);

    Page<AllUserResponseDto> getUsers(UserSortOption userSortOption, Pageable pageable);

    OneUserResponseDto getOneUser(Long userNo);

    Page<UserPostResponseDto> getUserAllPost(BoardType boardType, Long userNo,  Pageable pageable);

    void validateAdmin(String username);

}
