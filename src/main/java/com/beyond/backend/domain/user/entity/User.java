package com.beyond.backend.domain.user.entity;

import com.beyond.backend.domain.bookMark.entity.BookMark;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.common.entity.UserStatus;
import com.beyond.backend.domain.like.entity.Like;
import com.beyond.backend.domain.message.entity.Message;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.report.entity.Report;
import com.beyond.backend.domain.teamUser.entity.TeamUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long no;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    private UserRoleType role;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private Boolean Banned;

    @Column(nullable = false)
    private Integer passwordErrorCount;

    @Builder
    public User(String username, String password, String email, String phoneNum) {
        this.username = username;
        this.password = password;
        this.role = UserRoleType.USER;
        this.email = email;
        this.phoneNum = phoneNum;
        this.Banned = false;
        this.passwordErrorCount = 0;
        this.userStatus = UserStatus.ACTIVE;
    }

    public void updateUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateBan(boolean Banned) {
        this.Banned = Banned;
    }

    public void updatePasswordErrorCount(int passwordErrorCount) {
        this.passwordErrorCount = passwordErrorCount;
    }

    public void updateStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    // Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus; // ACTIVE, INACTIVE, DELETED

    // 유저 뱃지와 1:N 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBadge> userBadges = new HashSet<>();

    // 보낸 쪽지 리스트 (1:N 관계)
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Message> sentMessages = new HashSet<>();

    // 받은 쪽지 리스트 (1:N 관계)
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Message> receivedMessages = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user",  cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TeamUser> teamUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user",  cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<BookMark> bookmarks = new ArrayList<>();

    // 신고
    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Report> reportsMade = new HashSet<>();

    @OneToMany(mappedBy = "reported", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Report> reportsReceived = new HashSet<>();
}