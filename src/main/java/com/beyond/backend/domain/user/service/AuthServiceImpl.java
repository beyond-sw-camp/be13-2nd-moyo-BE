package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.dto.JoinRequestDto;
import com.beyond.backend.domain.user.dto.LoginRequestDto;
import com.beyond.backend.domain.user.dto.TokenResponseDto;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.jwt.JwtTokenProvider;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    @Transactional
    public void join(JoinRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (userRepository.existsByUsername(username)) {
            throw new UserException(ExceptionMessage.USER_DUPLICATE);
        }

        if (!password.equals(confirmPassword)) {
            throw new UserException(ExceptionMessage.USER_INPUT_MISMATCH);
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
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_INPUT_MISMATCH));

        validPwd(password, user);

        // 비밀번호가 일치한 경우 나머지 로그인 로직 실행
        TokenResponseDto tokenResponseDto = new TokenResponseDto(
                jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().toString()),
                jwtTokenProvider.createRefreshToken(user.getUsername()));

        // 추가 유효성 검사...
        CustomUserDetails customUserDetails = (CustomUserDetails) jwtTokenProvider
                .getAuthentication(tokenResponseDto.getAccessToken()).getPrincipal();

        if (!customUserDetails.isEnabled()) {
            throw new UserException(ExceptionMessage.USER_BANED);
        }
        if (!customUserDetails.isAccountNonLocked()) {
            throw new UserException(ExceptionMessage.USER_LOCKED);

        }

        return tokenResponseDto;
    }

    @Override
    @Transactional
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
    @Transactional
    public TokenResponseDto refresh(String bearerToken) {
        String refreshToken = jwtTokenProvider.resolveToken(bearerToken)
                .orElseThrow(() -> new IllegalArgumentException("Token is null"));

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        User user = userRepository.findByUsername(jwtTokenProvider.getUserName(refreshToken)).get();
        return new TokenResponseDto(jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().toString()),
                refreshToken);
    }

    // 비밀번호 검증 로직 (login 메서드 내에서 호출)
    public void validPwd(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            authTransactionService.increasePasswordErrorCount(user);
            log.info("Username : {}, PasswordErrorCount :0 {}", user.getUsername(), user.getPasswordErrorCount());
            throw new UserException(ExceptionMessage.USER_INPUT_MISMATCH);
        }
    }

    @Override
    public void validateEmail(String username, String email) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        if (!user.getEmail().equals(email)) {
            throw new UserException(ExceptionMessage.USER_INPUT_MISMATCH);
        }
    }
}
