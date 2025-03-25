package com.beyond.backend.domain.common.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    // 게시글 관련
    POST_NOT_FOUND("POST_001", "해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    // 게시글 비활성
    POST_ACCESS_DENIED("POST_002", "게시글에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    COMMENT_NOT_FOUND("POST_003", "댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),


    // 프로젝트 관련
    PROJECT_NOT_FOUND("PROJECT_001", "해당 프로젝트가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    TECH_NOT_FOUND("PROJECT_002", "해당 기술이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND("PROJECT_003", "해당 피드백이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 팀 관련
    TEAM_NOT_FOUND("TEAM_001", "해당하는 팀이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 메시지 관련
    MESSAGE_NOT_FOUND("MESSAGE_001", "해당 메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 유저 관련
    USER_NOT_FOUND("USER_001", "해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ACCESS_DENIED("USER_002", "해당 유저는 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    USER_INPUT_MISMATCH("USER_003", "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_LOCKED("USER_004", "계정이 잠겼습니다. 관리자에게 문의하세요.", HttpStatus.FORBIDDEN),
    USER_BANED("USER_005", "계정이 정지되었습니다. 관리자에게 문의하세요.", HttpStatus.FORBIDDEN),
    USER_DUPLICATE("USER_006", "이미 존재하는 유저입니다.", HttpStatus.BAD_REQUEST),

    // 신고 관련
    REPORT_NOT_FOUND("REPORT_001", "해당 신고가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 인증 관련
    INVALID_CREDENTIALS("AUTH_001", "아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_INVALID("AUTH_002", "토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("AUTH_003", "토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 공통
    INVALID_REQUEST("COMMON_001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("COMMON_002", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_REQUIRED("COMMON_003", "로그인이 필요한 기능입니다.", HttpStatus.UNAUTHORIZED);

    private final String code;     // 에러 코드 추가
    private final String message;
    private final HttpStatus status;
}
