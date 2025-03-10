package com.beyond.backend.domain.post.entity;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.post.entity
 * <p>fileName       : PostSortOption
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 9.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 9.        hyunjo             최초 생성
 */public enum PostSortOption {

    LATEST,     // 최신순(기본)
    BOOKMARK,   // 북마크순
    VIEW,        // 조회수순
    COMMENT      // 댓글 개수 순
}
