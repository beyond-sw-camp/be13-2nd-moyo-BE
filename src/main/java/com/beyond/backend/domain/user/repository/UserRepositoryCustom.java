package com.beyond.backend.domain.user.repository;

import com.beyond.backend.domain.user.entity.UserSortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beyond.backend.domain.user.dto.AllUserResponseDto;

public interface UserRepositoryCustom {
	Page<AllUserResponseDto> getUsers(UserSortOption userSortOption, Pageable pageable);



}
