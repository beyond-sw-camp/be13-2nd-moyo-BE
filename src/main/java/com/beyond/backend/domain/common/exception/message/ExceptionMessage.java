package com.beyond.backend.domain.common.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    //DEPARTMENT_NOT_FOUND("학과 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_INVALID("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;

    private final HttpStatus status;
}