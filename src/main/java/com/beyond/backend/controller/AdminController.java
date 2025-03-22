package com.beyond.backend.controller;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.service.CommentService;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.service.PostService;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
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
    private final CommentService commentService;
    private final ProjectService projectService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        authService.validateAdminByUsername(dto.getUsername());
        TokenResponseDto login = authService.login(dto);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/delete/{userNo}")
    public ResponseEntity<DeleteUserByAdminResponseDto> delete(@PathVariable Long userNo) {
        authService.validateAdminAuthorization();
        adminService.delete(userNo);
        DeleteUserByAdminResponseDto response = new DeleteUserByAdminResponseDto("User deleted successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<AllUserResponseDto>> getUsersByAdmin(
            @RequestParam(required = false) UserSortOption sortOption,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        authService.validateAdminAuthorization();
        Page<AllUserResponseDto> users = adminService.getUsers(sortOption, pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{userNo}")
    public ResponseEntity<OneUserResponseDto> getOneUserByAdmin(
            @PathVariable Long userNo) {

        authService.validateAdminAuthorization();
        OneUserResponseDto userResponse = adminService.getOneUser(userNo);
        return ResponseEntity.ok(userResponse);
    }

    // 유저의 게시글 가져오기
    @GetMapping("/user/{userNo}/post")
    public ResponseEntity<Page<UserPostResponseDto>> getUserPosts(
            @RequestParam BoardType boardType,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long userNo) {

        authService.validateAdminAuthorization();
        Page<UserPostResponseDto> userPosts = adminService.getUserAllPost(boardType,userNo, pageable);
        return ResponseEntity.ok(userPosts);
    }

    // 유저의 댓글 가져오기
    @GetMapping("/user/{userNo}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getUserComments(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                                    @PathVariable Long userNo) {

        authService.validateAdminAuthorization();
        Page<CommentResponseDto> commentList = commentService.getUserComments(userNo, pageable);
        return ResponseEntity.ok(commentList);
    }

    // 유저의 프로젝트 가져오기
    @GetMapping("/user/{userNo}/projects")
    public ResponseEntity<Page<ProjectResponseDto>> getProject(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                               @PathVariable Long userNo) {
        authService.validateAdminAuthorization();
        Page<ProjectResponseDto> projectList = projectService.getProjectsByUserNo(userNo, pageable);
        return ResponseEntity.ok(projectList);
    }
}