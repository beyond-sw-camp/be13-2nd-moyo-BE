package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.entity.UserRoleType;
import com.beyond.backend.domain.user.jwt.JwtTokenProvider;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTransactionService authTransactionService; // 별도 서비스 주입



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
                .email(dto.getEmail())
                .phoneNum(dto.getPhoneNum())
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다"));

        validPwd(password, user);

        // 비밀번호가 일치한 경우 나머지 로그인 로직 실행
        TokenResponseDto tokenResponseDto = new TokenResponseDto(
                jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().toString()),
                jwtTokenProvider.createRefreshToken(user.getUsername())
        );

        // 추가 유효성 검사...
        CustomUserDetails customUserDetails = (CustomUserDetails) jwtTokenProvider
                .getAuthentication(tokenResponseDto.getAccessToken()).getPrincipal();

        if (!customUserDetails.isEnabled()) {
            throw new RuntimeException("밴 사용자");
        }
        if (!customUserDetails.isAccountNonLocked()) {
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
                jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().toString()),
                refreshToken
        );
    }

    @Override
    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("유요하지 않은 토큰입니다");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        return null;
    }

    @Override
    public boolean isAdminFromUserDetails(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }

    // 비밀번호 검증 로직 (login 메서드 내에서 호출)
    public void validPwd(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // passwordErrorCount 를 별도의 트랜잭션에서 업데이트
            authTransactionService.increasePasswordErrorCount(user);
            log.info("Username : {}, PasswordErrorCount : {}", user.getUsername(), user.getPasswordErrorCount());
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다");
        }
    }
}