package com.beyond.backend.domain.message.entity;

import com.beyond.backend.domain.common.entity.BaseEntity;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "messages")
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Lob
    @Column(nullable = false)
    @NotEmpty(message = "내용을 작성해 주세요.")
    private String content;

    @JoinColumn(name = "sender_no") // NULL 허용
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL) //
    private User sender;

    @JoinColumn(name = "receiver_no") // NULL 허용
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL) // 삭제 시 NULL로 변경
    private User receiver; //직관적 네이밍

    @Column(nullable = false)
    private boolean deletedBySender;

    @Column(nullable = false)
    private boolean deletedByReceiver;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Builder
    public Message(String content, User sender, User receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;

        this.deletedBySender = false;
        this.deletedByReceiver = false;

        this.isRead = false;
    }

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
        markAsRead(); // 삭제했으니 읽음으로
    }

    public boolean isDeleted() {
        return isDeletedBySender() && isDeletedByReceiver();
    }

    public void markAsRead() {
        this.isRead = true;
    } // 읽음 안읽음

    public boolean hasPermission(Long userNo) {
        return (getReceiver() != null && userNo.equals(getReceiver().getNo())) ||
                (getSender() != null && userNo.equals(getSender().getNo()));
    }
}
