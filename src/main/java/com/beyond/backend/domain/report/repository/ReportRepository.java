package com.beyond.backend.domain.report.repository;

import com.beyond.backend.domain.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : UserReposrtRepository
 * <p>author         : mlnstone
 * <p>date           : 2025. 3. 3.
 * <p>description    :
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 3. 3.        mlnstone             최초 생성
*/

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findAllByReported_No(Long userNo, Pageable pageable);
}
