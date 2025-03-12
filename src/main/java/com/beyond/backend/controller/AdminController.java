package com.beyond.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.service.CommentService;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.service.PostService;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.service.ProjectService;
import com.beyond.backend.domain.user.dto.AllUserResponseDto;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminRequestDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;
import com.beyond.backend.domain.user.dto.OneUserResponseDto;
import com.beyond.backend.domain.user.service.AdminService;
import com.beyond.backend.domain.user.service.AuthService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final AdminService adminService;
    private final PostService postService;
    private final CommentService commentService;
    private final ProjectService projectService;

    @PostMapping("/deleteuser")
    public ResponseEntity<DeleteUserByAdminResponseDto> deleteUserByAdmin(@RequestBody DeleteUserByAdminRequestDto dto,
                                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        System.out.println(dto.getUserNo()+" us dsfs`````````````````````````");
        if (authService.isAdminFromUserDetails(userDetails)) {
            adminService.deleteUserByAdmin(dto);
            DeleteUserByAdminResponseDto response = new DeleteUserByAdminResponseDto("User deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            DeleteUserByAdminResponseDto response = new DeleteUserByAdminResponseDto("Unauthorized access.");
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Page<AllUserResponseDto>> getUsersByAdmin(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PageableDefault Pageable pageable ){


        if(authService.isAdminFromUserDetails(userDetails)){
            Page<AllUserResponseDto> users = adminService.getUsers(pageable);
            return ResponseEntity.ok(users);
        }

        return ResponseEntity.status(403).body(null);
    }

    @GetMapping("/user/{userNo}")
    public ResponseEntity<OneUserResponseDto> getOneUserByAdmin(@PathVariable Long userNo,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (authService.isAdminFromUserDetails(userDetails)) {
            OneUserResponseDto userResponse = adminService.getOneUser(userNo);
            return ResponseEntity.ok(userResponse);
        }

        return ResponseEntity.status(403).body(null);
    }

    // 유저의 게시글 가져오기
    @GetMapping("/user/{userNo}/post")
    public ResponseEntity<Page<UserPostResponseDto>> getUserPosts( @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PageableDefault(size = 10, page= 0) Pageable pageable,
                                                                @PathVariable Long userNo ) {


        if (authService.isAdminFromUserDetails(userDetails)) {
            Page<UserPostResponseDto> userPosts = postService.getUserPosts(userNo, pageable);
            return ResponseEntity.ok(userPosts);
        }

        return ResponseEntity.status(403).body(null);

    }

    // 유저의 댓글 가져오기
    @GetMapping("/user/{userNo}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getUserComments(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PageableDefault(size = 10, page= 0) Pageable pageable,
                                                                    @PathVariable Long userNo){


        if (authService.isAdminFromUserDetails(userDetails)) {
            Page<CommentResponseDto> commentList = commentService.getUserComments(userNo,pageable);
            return ResponseEntity.ok(commentList);
        }

        return ResponseEntity.status(403).body(null);
    }


    // 유저의 프로젝트 가져오기
    @GetMapping("/user/{userNo}/projects")
    public ResponseEntity<Page<ProjectResponseDto>> getProject( @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PageableDefault(size = 10, page= 0) Pageable pageable,
                                                                @PathVariable Long userNo ){

        if (authService.isAdminFromUserDetails(userDetails)) {
            Page<ProjectResponseDto> projectList = projectService.getProjectsByUserNo(userNo, pageable);
            return ResponseEntity.ok(projectList);
        }

        return ResponseEntity.status(403).body(null);
    }

    //신고목록 조회

    //신고 처리

}
