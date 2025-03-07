package com.beyond.backend.domain.common.entity;

import com.beyond.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private Long recipientNo;

    @Column(nullable = false)
    private NotificationStatus status;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean read;

    public void updateRead(boolean read) {
        this.read = read;
    }
}
