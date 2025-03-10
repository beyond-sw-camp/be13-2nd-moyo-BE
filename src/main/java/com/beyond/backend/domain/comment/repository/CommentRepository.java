package com.beyond.backend.domain.comment.repository;

import com.beyond.backend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : CommentRepository
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 4.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 4.        hyunjo             최초 생성
 */public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {


    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.no = :commentNo")
    int increaseLikeCount(@Param("commentNo") Long commentNo);

    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.no = :commentNo AND c.likeCount > 0")
    int decreaseLikeCount(@Param("commentNo") Long commentNo);

    @Query("SELECT c.likeCount FROM Comment c WHERE c.no = :commentNo")
    int getLatestLikeCount(@Param("commentNo") Long commentNo);
}
