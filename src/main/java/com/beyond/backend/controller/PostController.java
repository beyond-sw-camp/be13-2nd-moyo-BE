package com.beyond.backend.controller;

import com.beyond.backend.data.dto.ProjectDto;
import com.beyond.backend.data.dto.ProjectResponseDto;
import com.beyond.backend.data.dto.postDto.PostDto;
import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.dto.postDto.UserPostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.PostSearchOption;
import com.beyond.backend.data.entity.PostStatus;
import com.beyond.backend.data.entity.Status;
import com.beyond.backend.service.PostService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : PostController
 * <p>author         : hyunjo
 * <p>date           : 25. 2. 2.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 2. 2.        hyunjo             최초 생성
 * 25. 2. 17.       hyunjo             내용 수정
 * 25. 2. 20.       hyunjo             내용 수정
 */
@Tag(name = "게시판 API", description = "게시판 API")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    
    
    // 게시글 검색, 조회

    @Operation(summary = "게시글 전체 조회", description = "게시판 타입별로 게시글 조회")
    @GetMapping
    public ResponseEntity<?> getPosts(
            @RequestParam BoardType boardType,
            @PageableDefault(size = 10, page= 0)Pageable pageable) {
        Page<PostResponseDto> result = postService.getPosts(boardType, pageable);
        if (result.isEmpty()) {
            return ResponseEntity.ok("게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(result);
    }



    @Operation(summary = "게시글 단건 조회", description = "게시글 id로 조회")
    @GetMapping("/{postNo}")
    public ResponseEntity<?> getPostById(@PathVariable Long postNo) {
        PostResponseDto post = postService.getPostById(postNo);
        if (post == null) {
            return ResponseEntity.ok("해당 게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(post);
    }


    @Operation(summary = "게시글 검색", description = "제목, 내용, 작성자에서 검색어가 포함된 게시글 조회")
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam BoardType boardType,
            @RequestParam(required = false) PostSearchOption option,
            @RequestParam String keyword,
            @PageableDefault(size = 10, page= 0)Pageable pageable) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("검색어가 없습니다.");
        }

        Page<PostResponseDto> result = postService.searchPosts(boardType, option, keyword, pageable);

        if (result.isEmpty()) {
            return ResponseEntity.ok("게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(result);
    }
    
    //----------------------------------------------------------------------------
    // 게시글 생성 수정 (삭제 - 댓글 생각해야 함)

    //게시글 생성

    @Operation(summary = "게시글 등록", description = "게시글 등록 ")
    @PostMapping("/create")
    public ResponseEntity<PostResponseDto> createPost(
            @RequestParam BoardType boardType,
            @RequestBody PostDto postDto
    ) {

        PostResponseDto postResponseDto = postService.createPost(boardType,postDto);

        return ResponseEntity.ok(postResponseDto);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "제목, 내용, 게시글 상태 수정 가능")
    @PostMapping("/{postNo}")
    public ResponseEntity<PostResponseDto> updatePost(
            @RequestParam BoardType boardType,
            @RequestParam PostStatus postStatus,
            @PathVariable Long postNo,
            @RequestBody PostDto postDto

    ){

        PostResponseDto updatedPost = postService.updatePost(boardType, postStatus, postNo, postDto);

        return ResponseEntity.ok(updatedPost);
    }
    

    // 게시글 삭제 시 댓글 이 있는 경우 고려해야 함 ( 연관관계 메서드 생성)
    // 게시글 삭제 (작성자,관리자)
    
    // bordtype이랑 userNo 도 받아야 하고 comment required false로 받기
    // boardType은 애초에 관리자만 글을 생성할 수 있음 ( 일반유저는 글을 못 써서 회원 번호만 맞으면 지울 수 있게 하자 )
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping( "/{postNo}/delete")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postNo

    ) {


         postService.deletePost(postNo);

        return ResponseEntity.status(HttpStatus.OK).body("게시물이 삭제되었습니다.");
    }


    // 로그인한 유저가 작성한 게시글 리스트
    @Operation(summary = "유저가 작성한 게시글 전체 조회", description = "개인 페이지에서 자신의 게시글 전체 조회")
    @GetMapping("/user-info/my-post")
    public ResponseEntity<Page<UserPostResponseDto>> getMyPost(
            @RequestParam Long userNo,
            @PageableDefault(size = 10, page= 0)Pageable pageable

    ){
        // 내가 쓴 게시글을 게시판 종류를 나눠서 볼 수 있음,
        // 내가 쓴 게시글은 전체 조회랑 다르게 비활성 상태여도 볼 수 있음

        Page<UserPostResponseDto> result =  postService.getUserPosts(userNo,pageable);

       return ResponseEntity.ok(result);
    }




    //---------------------------------------------------------------------------

    // 북마크


    @Operation(summary = "게시글 북마크 추가 및 취소", description = "게시글 북마크 상태 (추가, 취소)")
    @PostMapping("/{postNo}/bookmark")
    public ResponseEntity<String> checkBookMark(
            @PathVariable Long postNo,
            @RequestParam Long userNo) {
        // 로그인된 세션에서 유저 번호 가져오기


        String result = postService.checkBookMark(postNo, userNo);
        return ResponseEntity.ok(result);
    }


    // 북마크된 게시글 전체 조회
    @Operation(summary = "북마크한 게시글 조회", description = "회원 ID로 북마크한 게시글을 게시판 타입별로 조회. 타입이 없으면 전체 조회.")
    @GetMapping("/bookmarks")
    public ResponseEntity<Page<UserPostResponseDto>> getBookmarkedPosts(
            @RequestParam Long userNo,
            @RequestParam(required = false) BoardType boardType, // boardType에 따라 북마크한 게시글을 나눠서 볼 수 있음 (전체 다 보려면 빼기 )
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<UserPostResponseDto> result = postService.getBookmarkedPosts(userNo, boardType , pageable);
        return ResponseEntity.ok(result);
    }


}
    
