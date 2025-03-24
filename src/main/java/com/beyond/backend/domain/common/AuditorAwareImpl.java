package com.beyond.backend.domain.common;

import com.beyond.backend.domain.user.dto.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null == authentication || !authentication.isAuthenticated()) {
            return Optional.empty(); // Optional.empty()로 수정
        }

        // 안전한 타입 확인
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails.getNo());
        } else {
            // CustomUserDetails가 아닌 경우 처리
            return Optional.empty();
        }
    }
}

