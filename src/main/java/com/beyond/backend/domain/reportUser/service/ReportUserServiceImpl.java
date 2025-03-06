package com.beyond.backend.domain.reportUser.service;

import com.beyond.backend.domain.reportUser.dto.ReportUserAdminResDto;
import com.beyond.backend.domain.reportUser.dto.ReportUserDto;
import com.beyond.backend.domain.reportUser.dto.ReportUserResponseDto;
import com.beyond.backend.domain.reportUser.entity.ReportUser;
import com.beyond.backend.domain.reportUser.repository.ReportUserRepository;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.service.impl
 * <p>fileName       : ReportServiceImpl
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

@Service
@RequiredArgsConstructor
public class ReportUserServiceImpl implements ReportUserService {

    private final UserRepository userRepository;
    private final ReportUserRepository reportUserRepository;

    @Override // 어드민만..
    public Page<ReportUserResponseDto> getReportList(Long userNo, Pageable pageable) {
        Page<ReportUser> reported = reportUserRepository.findAllByReported_No(userNo, pageable);

        return reported.map(reportUser -> ReportUserResponseDto.from(reportUser));
    }

    @Override
    @Transactional
    public ReportUserResponseDto createReport(ReportUserDto reportUserDto) {
        Optional<User> reporter = userRepository.findById(reportUserDto.getReporterNo());
        Optional<User> reported = userRepository.findById(reportUserDto.getReportedNo());
        if (reporter.isPresent() && reported.isPresent()) {
            ReportUser reportUser = ReportUser.builder()
                    .reportUserType(reportUserDto.getReportUserType())
                    .reporter(reporter.get())
                    .reported(reported.get())
                    .content(reportUserDto.getContent())
                    .build();
            reportUserRepository.save(reportUser);

            return ReportUserResponseDto.from(reportUser);
        } else {
            throw new RuntimeException("존재하지 않는 유저입니다");
        }
    }

    @Override
    @Transactional // Optional ReportUser도 체크안함아직
    public ReportUserResponseDto addComment(ReportUserAdminResDto reportUserAdminResDto) {
        Optional<User> reporter = userRepository.findById(reportUserAdminResDto.getReporterNo());
        Optional<User> reported = userRepository.findById(reportUserAdminResDto.getReportedNo());
        if (reporter.isPresent() && reported.isPresent()) {
            ReportUser reportUser = ReportUser.builder()
                    .no(reportUserAdminResDto.getNo())
                    .reportUserType(reportUserAdminResDto.getReportUserType())
                    .reporter(reporter.get())
                    .reported(reported.get())
                    .content(reportUserAdminResDto.getContent())
                    .comment(reportUserAdminResDto.getComment())
                    .build();
            reportUser.markAsCompleted();

            reportUserRepository.save(reportUser);
            return ReportUserResponseDto.from(reportUser);
        } else {
            throw new RuntimeException("존재하지 않는 유저입니다");

        }
    }

}
