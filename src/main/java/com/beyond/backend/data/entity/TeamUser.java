package com.beyond.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team_user")
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_no", nullable = false)
    private Team team;

    @Column
    private String role;

    private TeamJoinStatus status; // 승인여부

    @Column(nullable = false)
    private boolean isLeader;

    public void setStatus(TeamJoinStatus status) {
        this.status = status;
    }
}