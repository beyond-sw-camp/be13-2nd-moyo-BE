package com.beyond.backend.domain.post.repository;

import com.beyond.backend.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : PostRepository
 * <p>author         : hyunjo
 * <p>date           : 25. 2. 2.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 2. 2.        hyunjo             최초 생성
 */

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    // 트래픽이 중간이거나 적은 곳에서 동시성, 정합성 문제 해결하기에는 @Modifying + @Transactional 이 적당하다고 함


    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.no = :postNo")
    Optional<Post> findByIdWithUser(@Param("postNo") Long postNo);

    // 최신 북마크 수만 반환  jpql 사용 X 면 customrepository로 옮기기
    @Query("SELECT p.bookmarkCount FROM Post p WHERE p.no = :postNo")
    int getLatestBookmarkCount(@Param("postNo") Long postNo);

    // 북마크 개수 증가
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.bookmarkCount = p.bookmarkCount + 1 WHERE p.no = :postNo")
    int increaseBookmark(@Param("postNo") Long postNo);

    // 북마크 개수 감소
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.bookmarkCount = p.bookmarkCount - 1 WHERE p.no = :postNo AND p.bookmarkCount > 0")
    int decreaseBookmark(@Param("postNo") Long postNo);


    // 조회수 증가 코드
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.no = :postNo")
    void increaseViewCount(@Param("postNo") Long postNo);


    // 최신 댓글 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.no = :postNo")
    int getLatestCommentCount(@Param("postNo") Long postNo);

    // 댓글 개수 증가
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + 1 WHERE p.no = :postNo")
    int increaseCommentCount(@Param("postNo") Long postNo);

    // 댓글 개수 감소
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.commentCount = p.commentCount - 1 WHERE p.no = :postNo AND p.commentCount > 0")
    int decreaseCommentCount(@Param("postNo") Long postNo);

    void deleteByUserNo(Long userNo);
}