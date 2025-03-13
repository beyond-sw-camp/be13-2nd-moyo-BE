package com.beyond.backend.controller;

import com.beyond.backend.domain.comment.dto.CommentDto;
import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.CommentSortOption;
import com.beyond.backend.domain.comment.service.CommentService;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : CommentController
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 3.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 3.        hyunjo             최초 생성
 */
@Tag(name = "댓글 API", description = "댓글 API")    
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // boardType Free에서만 가능하게 하기
    @Operation(summary = "댓글 생성", description = "댓글 등록")
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CommentDto commentDto) {

        CommentResponseDto commentResponseDto = commentService.createComment(commentDto, userDetails.getUser().getNo());
        return ResponseEntity.ok(commentResponseDto);
    }


    // 로그인한 유저만 가능하게 하기
    @Operation(summary = "댓글 수정", description = "댓글 내용 수정")
    @PostMapping("/comments/{commentNo}/update")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentNo,
            @Valid @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        CommentResponseDto updateComment = commentService.updateComment(commentNo, commentDto,  userDetails.getUser().getNo());

        return ResponseEntity.ok(updateComment);
    }



    /*
     * 게시글 삭제 시 댓글도 같이 삭제됨
     * 게시글이 비활성화 된 상태인 경우 유저는 자신의 댓글을 볼 수 있고 삭제 가능 but 게시글 내용은 볼 수 없음
     * 비활성화된 게시글에서 내 댓글을 지우고 싶은 경우 내가 댓글을 쓴 게시글 리스트에서는 비활성화된 게시글이라고 알려주고
     * 내가 쓴 댓글 전체 조회에서 삭제 가능하게 하자
     *
     * 게시글 활성화 상태에서는 내가 쓴 댓글 전체 조회에서 삭제 할 수 있고
     * */

    // 관리자와 댓글 작성자만 삭제 가능 ( 비활성 상태인 경우 안 보임, 유저가 상태가 어떻든 게시글이 활성 상태이면 볼 수 있음, 게시글이 비활성이면 못 봄 )
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/comments/{commentNo}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

            commentService.deleteComment(commentNo,  userDetails.getUser().getNo());

        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }




    // 댓글도 페이징 처리
    // 게시글의 모든 댓글 조회
    @Operation(summary = "게시글의 전체 댓글 조회", description = "게시글에 작성된 전체 댓글 조회")
    @GetMapping("/posts/{postNo}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getPostComments(
            @PathVariable Long postNo,
            @RequestParam(required = false) CommentSortOption commentSortOption,
            @PageableDefault(size = 10, page= 0) Pageable pageable){

        Page<CommentResponseDto> result = commentService.getPostComments(postNo, commentSortOption, pageable);

        return ResponseEntity.ok(result);
    }




    // 내가 쓴 댓글 전체 조회
    @Operation(summary = "유저의 댓글 전체 조회", description = "개인 페이지에서 자신의 댓글을 전체 조회 가능")
    @GetMapping("/user-page/comments")
    public ResponseEntity<Page<CommentResponseDto>> getUserComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, page= 0) Pageable pageable){

        Page<CommentResponseDto> result = commentService.getUserComments(userDetails.getUser().getNo(),pageable);

        return ResponseEntity.ok(result);
    }


    // 내가 댓글 단 게시글 전체 조회
    @Operation(summary = "유저가 댓글단 게시글 전체 조회", description = "개인 페이지에서 자신이 댓글단 게시글 전체 조회 가능")
    @GetMapping("/users/comments/posts")
    public ResponseEntity<Page<PostResponseDto>> getUserCommentPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, page= 0) Pageable pageable){

        Page<PostResponseDto> result = commentService.getUserCommentPosts(userDetails.getUser().getNo(), pageable);

        return ResponseEntity.ok(result);
    }



    //------------------------

    // 댓글 좋아요 / 좋아요 취소
    @Operation(summary = "댓글 좋아요 추가 및 취소", description = "댓글 좋아요 상태 (추가, 취소)")
    @PostMapping("/{commentNo}/like")
    public ResponseEntity<String> likeComment(
            @PathVariable Long commentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        String result = commentService.checkCommentLike(commentNo, userDetails.getUser().getNo());


        return ResponseEntity.ok(result);

    }

    // 유저가 좋아요한 댓글 전체 조회
    @Operation(summary = "유저가 좋아요한 댓글 전체 조회", description = "개인 페이지에서 자신이 좋아요한 댓글 전체 조회 가능")
    @GetMapping("/users/liked-comments")
    public ResponseEntity<Page<CommentResponseDto>> getUserLikedComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, page= 0) Pageable pageable){


        Page<CommentResponseDto> result = commentService.getUserLikedComments(userDetails.getUser().getNo(), pageable);

        return ResponseEntity.ok(result);
    }







}



