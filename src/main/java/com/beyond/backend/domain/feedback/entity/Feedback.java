package com.beyond.backend.domain.feedback.entity;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "feedbacks",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_retrospective",
                                  columnNames = {"user_no", "project_no", "feedbackType"})
        })
public class Feedback extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private String content;

    //팀원이 썼는지는 서비스 로직에서 체크
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_no", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackType feedbackType;

    //== 연관관계 메서드 ==//
    public void setProject(Project project){
        this.project = project;
        if (!project.getFeedbacks().contains(this)){
            project.getFeedbacks().add(this);
        }
    }

    public void updateFeedback(String content) {
        this.content = content;
    }

}
