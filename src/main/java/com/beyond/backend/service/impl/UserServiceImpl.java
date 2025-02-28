package com.beyond.backend.service.impl;

import com.beyond.backend.data.dto.userDto.LoginDto;
import com.beyond.backend.data.dto.userDto.UserSignUpDto;
import com.beyond.backend.data.entity.Status;
import com.beyond.backend.data.entity.User;
import com.beyond.backend.data.repository.UserRepository;
import com.beyond.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional//회원 가입
    public Long join(UserSignUpDto userDto) {
        validateDuplicateUser(userDto);
        User user = User.builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .phoneNum(userDto.getPhoneNum())
                .address(userDto.getAddress())
                .status(Status.ACTIVE)
                .build();
        userRepository.save(user);
        return user.getNo();
    }

    private void validateDuplicateUser(UserSignUpDto userDto) {
        boolean exists = userRepository.existsByUsername(userDto.getUsername());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 회원");
        }
    }

    @Override
    public LoginDto login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new LoginDto(user);
    }
}
