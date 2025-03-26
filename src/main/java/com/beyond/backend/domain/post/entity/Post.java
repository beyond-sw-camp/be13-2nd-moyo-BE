package com.beyond.backend.domain.post.entity;

import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.common.entity.BaseEntity;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 당장은 필요없지만 확장성 위해
    private Long no;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String postContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus postStatus;
    private int viewCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    private int bookmarkCount = 0;

    private int commentCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;



    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();



    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMark> bookmarks = new ArrayList<>();


    @Builder
    public Post(String postTitle, String postContent, BoardType boardType, User user, PostStatus postStatus) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.boardType = boardType;
        this.user = user;
        this.postStatus = postStatus;
    }


    // 연관관계 편의 메서드 -------------------------

    // 댓글 추가 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void update(String postTitle, String postContent, PostStatus postStatus, BoardType boardType){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postStatus = postStatus;
        this.boardType = boardType;

    }
}