package com.beyond.backend.data.entity;

import io.swagger.v3.oas.annotations.info.Info;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 당장은 필요없지만 확장성 위해
    private Long no;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String postContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus postStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Enumerated(EnumType.STRING)
    private PostSearchOption searchOption;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;


    //============================

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();


    //좋아요 연관관계 로직
    public int getLikeCount() {
        return likes.size();
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMark> bookmarks = new ArrayList<>();


    protected Post() {
    }

  /*  // ✅ 댓글 추가 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    // ✅ 댓글 삭제 연관관계 편의 메서드
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }

*/



    //게시글 수정 용 메서드
    public void update(String postTitle, String postContent, PostStatus postStatus){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postStatus = postStatus;

    }


}
