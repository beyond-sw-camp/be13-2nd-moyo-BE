package com.beyond.backend.domain.comment.entity;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    protected Comment(){

    }

    // 빌더 패턴을 안 쓰고 명시적으로 생성자를 만들어줌
    public Comment(String content, Post post, User user){
        this.content = content;
        setPost(post);
        setUser(user);
    }

    //----------------------------------

    //( 댓글 )연관관계 편의 메서드

    private void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    private void setUser(User user) {
        this.user = user;
        // user.getComments().add(this);
    }
    //--------------------------------

    // 댓글 수정 메서드
    public void update(String content){

        this.content = content;
    }

}