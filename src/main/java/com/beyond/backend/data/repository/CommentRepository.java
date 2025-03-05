package com.beyond.backend.data.repository;

import com.beyond.backend.data.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
