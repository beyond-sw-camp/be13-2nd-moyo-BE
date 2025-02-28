package com.beyond.backend.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
public class BookMark {

    @EmbeddedId
    private BookMarkNo no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no",  insertable = false, updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", insertable = false, updatable = false)
    private User user;

    protected BookMark(){

    }


}