package com.beyond.backend.domain.user.entity;

import com.beyond.backend.domain.tech.entity.Tech;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tech", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_no", "tech_no"})
})
public class UserTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_no", nullable = false)
    private Tech tech;
}