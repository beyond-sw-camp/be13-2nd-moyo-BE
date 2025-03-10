package com.beyond.backend.domain.like.entity;

import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"comment_no", "user_no"})
})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_no")
    private Long no;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_no", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    protected Like(){

    }

    public Like (Comment comment, User user){
        this.comment = comment;
        this.user = user;
    }
}

