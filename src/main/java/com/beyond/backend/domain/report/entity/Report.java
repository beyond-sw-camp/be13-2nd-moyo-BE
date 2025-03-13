package com.beyond.backend.domain.report.entity;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.data.entity
 * <p>fileName       : Report
 * <p>author         : mlnstone
 * <p>date           : 2025. 3. 3.
 * <p>description    : 신고 테이블
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 3. 3.        mlnstone             최초 생성
*/

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "report")
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    // N+1
    @JoinColumn(name = "reporter_no")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User reporter;


    @JoinColumn(name = "reported_no")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User reported;

    @Column
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name = "report_status")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    @Column
    private String url;

    @Lob
    @Column
    @NotEmpty(message = "내용을 작성해 주세요.")
    private String content;

    @Lob
    @Column
    private String comment;

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void updateReportStatus(ReportStatus status) {
        this.reportStatus = status;
    }
}
