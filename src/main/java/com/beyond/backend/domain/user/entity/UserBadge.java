package com.beyond.backend.domain.user.entity;

import com.beyond.backend.domain.common.entity.Badge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_badges", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_no", "badge_no"}) // 같은 뱃지를 중복 보유 방지
})
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_no", nullable = false)
    private Badge badge;
}
