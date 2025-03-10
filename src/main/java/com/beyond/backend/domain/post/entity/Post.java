package com.beyond.backend.domain.post.entity;

import com.beyond.backend.domain.bookMark.entity.BookMark;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 당장은 필요없지만 확장성 위해
    private Long no;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String postContent;

    @Column(nullable = false)
    private int viewCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus postStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;


    @Column(nullable = false)
    private int bookmarkCount;

    @Column(nullable = false)
    private int commentCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;



    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();



    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMark> bookmarks = new ArrayList<>();



    //-------------------------------------------------------

    protected Post() {
    }


    // 연관관계 편의 메서드 -------------------------

    // 댓글 추가 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }


    //------------------------------------------

    //게시글 수정 용 메서드
    public void update(String postTitle, String postContent, PostStatus postStatus){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postStatus = postStatus;

    }


}
