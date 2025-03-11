package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.common.entity.UserStatus;
import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserUpdateResponseDto updateUser(Long id, UserUpdateRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateUser(dto.getUsername(), dto.getEmail());

        userRepository.save(user);
        return new UserUpdateResponseDto(user);
    }

    @Override
    public PasswordUpdateResponseDto updatePassword(String username, PasswordUpdateRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.updatePassword(encodedPassword);

        userRepository.save(user);

        return new PasswordUpdateResponseDto(); // 비밀번호 body 에 노출 위험 -> 메시지 반환
    }

    //softDelete
    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    @Override
    public BanResponseDto banUser(BanRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateBan(dto.getBan());
        userRepository.save(user);
        return new BanResponseDto();
    }

    @Override
    public UnlockResponseDto unlockUser(UnlockRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.updatePasswordErrorCount(0);
        userRepository.save(user);
        return new UnlockResponseDto();
    }
}
