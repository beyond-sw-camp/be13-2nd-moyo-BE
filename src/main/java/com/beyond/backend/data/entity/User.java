package com.beyond.backend.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false, unique = true, length = 50)
    private String username; //아이디

    @Column(nullable = false)
    private String name; //성명

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private String phoneNum;

    @Builder
    public User(String username, String name, String password, String phoneNum, String address, String email, Status status ) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
        this.address = address;
        this.email = email;
        this.status = status;

    }

    private String address;

    //Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; //ACTIVE, INACTIVE, DELETED


    private String email;

    //유저 뱃지와 1:N 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBadge> userBadges = new HashSet<>();

    // 보낸 쪽지 리스트 (1:N 관계)
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> sentMessages = new HashSet<>();

    // 받은 쪽지 리스트 (1:N 관계)
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> receivedMessages = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    // [홍재민] 팀 구성과 연결
    @OneToMany(mappedBy = "user")
    private List<TeamUser> teamUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMark> bookmarks;


    // @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<Post> posts = new HashSet<>();
}
