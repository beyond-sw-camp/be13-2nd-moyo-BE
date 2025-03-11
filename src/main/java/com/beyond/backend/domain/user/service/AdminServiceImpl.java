package com.beyond.backend.domain.user.service;

import org.springframework.stereotype.Service;

import com.beyond.backend.domain.user.dto.DeleteUserByAdminRequestDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public DeleteUserByAdminResponseDto DeleteUserByAdmin(DeleteUserByAdminRequestDto dto) {

        User user = userRepository.findById(dto.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
                
        userRepository.delete(user);

        return new DeleteUserByAdminResponseDto();
    }

}