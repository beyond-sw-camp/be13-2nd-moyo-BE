package com.beyond.backend.domain.bookMark.entity;

import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
public class BookMark {


    @EmbeddedId
    private BookMarkNo no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no", insertable = false, updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", insertable = false, updatable = false)
    private User user;

    protected BookMark(){

    }


}