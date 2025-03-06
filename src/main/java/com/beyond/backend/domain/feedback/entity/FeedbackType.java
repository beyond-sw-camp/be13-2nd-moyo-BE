package com.beyond.backend.domain.feedback.entity;

public enum FeedbackType {
    TROUBLESHOOTING,  // 트러블 슈팅
    WEAKNESS,         // 아쉬운 점
    LESSON,           // 배운 점
    RETROSPECTIVE     // 회고록 (팀원당 1개만 작성 가능)
}
