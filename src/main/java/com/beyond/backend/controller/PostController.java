package com.beyond.backend.controller;

import com.beyond.backend.domain.post.dto.PostDto;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.PostSearchOption;
import com.beyond.backend.domain.post.entity.PostSortOption;
import com.beyond.backend.domain.post.entity.PostStatus;
import com.beyond.backend.domain.post.service.PostService;
import com.beyond.backend.domain.user.service.UserService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    
    
    // 게시글 전체 조회
    // 게시글 정렬 조건 ( 기본이 최신순)

    @Operation(summary = "게시글 전체 조회", description = "게시판 타입별로 게시글 조회")
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getPosts(
            @RequestParam BoardType boardType,
            @RequestParam(required = false) PostSortOption postSortOption,
            @PageableDefault(size = 10, page= 0)Pageable pageable) {
        Page<PostResponseDto> allPosts = postService.getPosts(boardType, pageable, postSortOption);
       
      /* 
        if (allPosts.isEmpty()) {
            return ResponseEntity.ok("게시글이 존재하지 않습니다.");
        } */// 실무에서는 컨트롤러에서 직접 경고 메시지를 보내는 것을 지양한다
        // NOT_FOUND 오류처리
        return ResponseEntity.ok(allPosts);
    }


    // 게시글 단 건 조회 
    @Operation(summary = "게시글 단건 조회", description = "활성화된 게시글 상세 조회")
    @GetMapping("/posts/{postNo}")
    public ResponseEntity<?> getPostById(@PathVariable Long postNo) {
        PostResponseDto post = postService.getPostById(postNo);
        if (post == null) {
            return ResponseEntity.ok("해당 게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(post);
    }


    @Operation(summary = "게시글 검색", description = "제목, 내용, 작성자에서 검색어가 포함된 게시글 조회")
    @GetMapping("/posts/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam BoardType boardType,
            @RequestParam(required = false) PostSearchOption option,
            @RequestParam String keyword,
            @PageableDefault(size = 10, page= 0)Pageable pageable) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("검색어가 없습니다.");
        }

        Page<PostResponseDto> searchPosts = postService.searchPosts(boardType, option, keyword, pageable);

        if (searchPosts.isEmpty()) {
            return ResponseEntity.ok("게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(searchPosts);
    }
    
    //----------------------------------------------------------------------------
    // 게시글 생성 수정 (삭제 - 댓글 생각해야 함)

    //게시글 생성

    @Operation(summary = "게시글 등록", description = "게시글 등록 ")
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(
            @RequestParam BoardType boardType,
            @RequestBody PostDto postDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostResponseDto postResponseDto = postService.createPost(boardType, postDto, userDetails.getUser().getNo());
        return ResponseEntity.ok(postResponseDto);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "제목, 내용, 게시글 상태 수정 가능")
    @PostMapping("/posts/{postNo}")
    public ResponseEntity<PostResponseDto> updatePost(
            @RequestParam BoardType boardType,
            @RequestParam PostStatus postStatus,
            @PathVariable Long postNo,
            @RequestBody PostDto postDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        PostResponseDto updatedPost = postService.updatePost(boardType, postStatus, postNo, postDto, userDetails.getUser().getNo());
        return ResponseEntity.ok(updatedPost);
    }
    

    // 게시글 삭제 시 댓글 이 있는 경우 고려해야 함 ( 연관관계 메서드 생성)
    // 게시글 삭제 (작성자,관리자)
    
    // bordtype이랑 userNo 도 받아야 하고 comment required false로 받기
    // boardType은 애초에 관리자만 글을 생성할 수 있음 ( 일반유저는 글을 못 써서 회원 번호만 맞으면 지울 수 있게 하자 )
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping( "/posts/{postNo}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postNo

    ) {
         postService.deletePost(postNo);

        return ResponseEntity.status(HttpStatus.OK).body("게시물이 삭제되었습니다.");
    }


    // 로그인한 유저가 작성한 게시글 리스트
    @Operation(summary = "유저가 작성한 게시글 전체 조회", description = "개인 페이지에서 자신의 게시글 전체 조회")
    @GetMapping("/user/post")
    public ResponseEntity<Page<UserPostResponseDto>> getMyPost(
            @RequestParam Long userNo,
            @PageableDefault(size = 10, page= 0)Pageable pageable

    ){
        // 내가 쓴 게시글을 게시판 종류를 나눠서 볼 수 있음,
        // 내가 쓴 게시글은 전체 조회랑 다르게 비활성 상태여도 볼 수 있음

        Page<UserPostResponseDto> result =  postService.getUserPosts(userNo,pageable);
        
        // 조회 결과가 없는 경우 처리

       return ResponseEntity.ok(result);
    }




    //---------------------------------------------------------------------------
    // 북마크


    @Operation(summary = "게시글 북마크 추가 및 취소", description = "게시글 북마크 상태 (추가, 취소)")
    @PostMapping("/posts/{postNo}/bookmark")
    public ResponseEntity<String> checkBookMark(
            @PathVariable Long postNo,
            @RequestParam Long userNo) {
        // 로그인된 세션에서 유저 번호 가져오기


        String result = postService.checkBookMark(postNo, userNo);
        return ResponseEntity.ok(result);
    }


    // 유저가 북마크한 게시글 전체 조회
    @Operation(summary = "북마크한 게시글 조회", description = "유저가 북마크한 게시글을 게시판 타입별로 조회. 타입이 없으면 전체 조회.")
    @GetMapping("/user-page/{userNo}/bookmark")
    public ResponseEntity<Page<UserPostResponseDto>> getBookmarkedPosts(
            @PathVariable Long userNo,
            @RequestParam(required = false) BoardType boardType, // boardType에 따라 북마크한 게시글을 나눠서 볼 수 있음 (전체 다 보려면 빼기 )
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<UserPostResponseDto> result = postService.getBookmarkedPosts(userNo, boardType , pageable);
        return ResponseEntity.ok(result);
    }


}
    
