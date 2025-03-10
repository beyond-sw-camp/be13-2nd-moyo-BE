package com.beyond.backend.domain.report.entity;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.data.entity
 * <p>fileName       : ReportUserType
 * <p>author         : mlnstone
 * <p>date           : 2025. 3. 3.
 * <p>description    :
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 3. 3.        mlnstone             최초 생성
*/

public enum ReportType {
    USER_REPORT,    // 유저 신고
    POST_REPORT,    // 게시글 신고
    MESSAGE_REPORT,  // 쪽지 신고
    OTHER           // 기타
}