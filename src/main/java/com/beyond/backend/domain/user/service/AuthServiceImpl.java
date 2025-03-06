package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.entity.UserRoleType;
import com.beyond.backend.domain.user.jwt.JwtTokenProvider;
import com.beyond.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void join(JoinRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (userRepository.existsByUsername(username)) {
            return;
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("New passwords don't match");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .role(UserRoleType.USER)
                .email(dto.getEmail())
                .phoneNum(dto.getPhoneNum())
                .build();

        userRepository.save(user);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다"));

        if (!passwordEncoder.matches(password, user.getPassword())) {

            user.updatePasswordErrorCount(user.getPasswordErrorCount() + 1);
            userRepository.save(user);
            log.info("{} pwdErrCount = {}", user.getUsername(), user.getPasswordErrorCount());
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다");
        }

        //회원정보 유효성 검증 후 토큰 발급

        TokenResponseDto tokenResponseDto = new TokenResponseDto(
                jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().toString()),
                jwtTokenProvider.createRefreshToken(user.getUsername())
        );

        // 스프링 시큐리티 필터를 통해서 로그인이 처리되었으면
        // CustomUserDetails 안에 유저정보가 들어있으니까 그걸통해서 밴여부를 확인하는거져
        // 사용자 밴 여부 확인 로직 이부분 메서드로 빼서 쓰시면 깔끔할듯
        CustomUserDetails customUserDetails = (CustomUserDetails) jwtTokenProvider.getAuthentication(tokenResponseDto.getAccessToken()).getPrincipal();

        if(!customUserDetails.isEnabled()){
            System.out.println("너 밴");
            throw new RuntimeException("밴 사용자");
        }

        if (!customUserDetails.isAccountNonLocked()) {
            System.out.println("너 임시 정지");
            throw new RuntimeException("임시 정지 사용자");
        }
        return tokenResponseDto;
    }

    @Override
    public void logout(String bearerToken) {
        String accessToken = jwtTokenProvider.resolveToken(bearerToken)
                .orElseThrow(() -> new IllegalArgumentException("Token is null"));

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        jwtTokenProvider.addBlacklist(accessToken);
        jwtTokenProvider.deleteRefreshToken(accessToken);
    }

    @Override
    public TokenResponseDto refresh(String bearerToken) {
        String refreshToken = jwtTokenProvider.resolveToken(bearerToken)
                .orElseThrow(() -> new IllegalArgumentException("Token is null"));

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        User user = userRepository.findByUsername(jwtTokenProvider.getUserName(refreshToken)).get();

        return new TokenResponseDto(
                jwtTokenProvider.createAccessToken(user.getUsername(),
                                                   user.getRole().toString()),
                                                   refreshToken
        );
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