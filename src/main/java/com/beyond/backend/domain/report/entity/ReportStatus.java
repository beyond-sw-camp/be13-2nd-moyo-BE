package com.beyond.backend.domain.report.entity;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.domain.report.entity
 * <p>fileName       : ReportStatus
 * <p>author         : mlnstone
 * <p>date           : 2025. 3. 12.
 * <p>description    :
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 3. 12.        mlnstone             최초 생성
*/

public enum ReportStatus {
    PENDING,      // 처리 중
    COMPLETED,      // 처리 완료
    ONLY_BANNED,   // 사용자 밴 (게시글 유지),
    BANNED       // 사용자 밴 + 모든 작성한 글(게시글+댓글) 삭제
}