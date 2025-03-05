package com.beyond.backend.controller;

import com.beyond.backend.data.dto.commentDto.CommentDto;
import com.beyond.backend.data.dto.commentDto.CommentResponseDto;
import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.PostStatus;
import com.beyond.backend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            @RequestBody CommentDto commentDto
    ) {
        return ResponseEntity.ok(commentService.createComment(commentDto));
    }


    // 로그인한 유저만 가능하게 하기
    @Operation(summary = "댓글 수정", description = "댓글 내용 수정")
    @PostMapping("/comments/{commentNo}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentNo,
            @RequestBody CommentDto commentDto

    ) {
        return ResponseEntity.ok(commentService.updateComment(commentNo, commentDto));
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
    public ResponseEntity<String> deleteComment(@PathVariable Long commentNo, Long userNo) {
        commentService.deleteComment(commentNo, userNo);

        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }




    // 댓글도 페이징 처리?
    // 게시글의 모든 댓글 조회
    
    
    // 내가 쓴 댓글 전체 조회
    @Operation(summary = "유저의 댓글 전체 조회", description = "개인 페이지에서 자신의 댓글을 전체 조회 가능")
    @GetMapping("/{userNo}/user-page/comments")
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Long userNo,
            @PageableDefault(size = 10, page= 0) Pageable pageable){

       Page<CommentResponseDto> result = commentService.getUserComments(userNo,pageable);

        return ResponseEntity.ok(result);
    }

    
    // 내가 댓글 단 게시글 전체 조회







}




