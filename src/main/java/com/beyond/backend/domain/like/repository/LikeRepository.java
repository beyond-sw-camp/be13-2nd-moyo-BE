package com.beyond.backend.domain.like.repository;

import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.like.entity.Like;
import com.beyond.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.like.repository
 * <p>fileName       : LikeRepository
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 6.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 6.        hyunjo             최초 생성
 */public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

    Like findByCommentAndUser(Comment comment, User user);
}
