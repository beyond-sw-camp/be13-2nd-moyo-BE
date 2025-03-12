package com.beyond.backend.domain.comment.entity;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.like.entity.Like;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

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


    private int likeCount=0;
    // 조회하는 경우가 많아서 필드로 만들어줌



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;


    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();


    //좋아요 연관관계 로직 -> 데이터 정합성 문제 발생 db에서 직접 조회하기
  /*  public int getLikeCount() {
        return likes.size();
    }*/

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

    public void setPost(Post post) {
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