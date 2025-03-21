package com.beyond.backend.domain.comment.repository;

import com.beyond.backend.domain.comment.entity.Like;
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
 */public interface LikeRepository extends JpaRepository<Like, Long> {


}
