package com.beyond.backend.domain.report.entity;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_no")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "reported_no")
    private User reported;

    @Column
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column
    private String url;

    @Lob
    @Column
    private String content;

    @Lob
    @Column
    private String comment;

    @Column(name = "is_completed")
    private boolean isCompleted;

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

}
