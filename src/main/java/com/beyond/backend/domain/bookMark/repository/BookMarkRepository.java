package com.beyond.backend.domain.bookMark.repository;

import com.beyond.backend.domain.bookMark.entity.BookMark;
import com.beyond.backend.domain.bookMark.entity.BookMarkNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : BookMarkRepository
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 1.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 1.        hyunjo             최초 생성
 */
public interface BookMarkRepository extends JpaRepository<BookMark, BookMarkNo>, BookMarkRepositoryCustom {





}