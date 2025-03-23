package com.beyond.backend.controller;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.service.CommentService;
import com.beyond.backend.domain.feedback.dto.FeedbackResponseDto;
import com.beyond.backend.domain.feedback.service.FeedbackService;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.service.PostService;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.entity.ProjectSortOption;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.project.service.ProjectService;
import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.UserSearchOption;
import com.beyond.backend.domain.user.entity.UserSortOption;
import com.beyond.backend.domain.user.service.AdminService;
import com.beyond.backend.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "10 관리자 API", description = "관리자 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final AdminService adminService;
    private final PostService postService;
    private final CommentService commentService;
    private final ProjectService projectService;
    private final FeedbackService feedbackService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        TokenResponseDto login = authService.login(dto);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/delete/{userNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DeleteUserByAdminResponseDto> delete(@PathVariable Long userNo,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminService.delete(userNo);
        DeleteUserByAdminResponseDto response = new DeleteUserByAdminResponseDto("삭제가 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<AllUserResponseDto>> getUsersByAdmin(
            @RequestParam(required = false) UserSortOption sortOption,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<AllUserResponseDto> users = adminService.getUsers(sortOption, pageable);

        return ResponseEntity.ok(users);

    }

    @GetMapping("/user/{userNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OneUserResponseDto> getOneUserByAdmin(
            @PathVariable Long userNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        OneUserResponseDto userResponse = adminService.getOneUser(userNo);
        return ResponseEntity.ok(userResponse);
    }

    // 유저의 게시글 가져오기
    @GetMapping("/user/{userNo}/posts")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserPostResponseDto>> getUserPosts(
            @RequestParam BoardType boardType,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long userNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<UserPostResponseDto> userPosts = adminService.getUserAllPost(boardType,userNo, pageable);
        return ResponseEntity.ok(userPosts);

    }

    // 유저의 댓글 가져오기
    @GetMapping("/user/{userNo}/comments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<CommentResponseDto>> getUserComments(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                                    @PathVariable Long userNo,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<CommentResponseDto> commentList = commentService.getUserComments(userNo, pageable);
        return ResponseEntity.ok(commentList);
    }


    // 유저의 프로젝트 가져오기
    @GetMapping("/user/{userNo}/projects")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ProjectResponseDto>> getProject(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                               @PathVariable Long userNo,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<ProjectResponseDto> projectList = projectService.getProjectsByUserNo(userNo, pageable);
        return ResponseEntity.ok(projectList);
    }


    @GetMapping("/feedbacks")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getFeedback(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<FeedbackResponseDto> feedbackList = feedbackService.getAllFeedback(pageable);
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/projects")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ProjectResponseDto>> getProject(
                                                                @RequestParam ProjectSortOption projectSortOption,
                                                                @PageableDefault(size = 10, page = 0) Pageable pageable,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<ProjectResponseDto> projectList = projectService.getAllProjects(pageable, projectSortOption);
        return ResponseEntity.ok(projectList);
    }


}
