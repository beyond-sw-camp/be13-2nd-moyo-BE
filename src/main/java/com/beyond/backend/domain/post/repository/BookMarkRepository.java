package com.beyond.backend.domain.post.repository;

import com.beyond.backend.domain.post.entity.BookMark;
import com.beyond.backend.domain.post.entity.BookMarkNo;
import org.springframework.data.jpa.repository.JpaRepository;

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