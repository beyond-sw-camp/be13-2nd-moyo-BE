package com.beyond.backend.data.dto.ReportUserDto;

import com.beyond.backend.data.entity.ReportUser;
import com.beyond.backend.data.entity.ReportUserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : ReportResponseDto
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportUserResponseDto {
    private Long no; // 신고 번호

    private String ReporterId;
    private String ReportedId;

    private ReportUserType ReportUserType;
    private String content;
    private String comment;

    private boolean isCompleted;

    private LocalDateTime reportTime;

    public static ReportUserResponseDto from(ReportUser reportUser) {
        return new ReportUserResponseDto(
                reportUser.getNo(),
                reportUser.getReporter().getUsername(),
                reportUser.getReported().getUsername(),
                reportUser.getReportUserType(),
                reportUser.getContent(),
                reportUser.getComment(),
                reportUser.isCompleted(),
                reportUser.getCreatedAt()
        );
    }

}
