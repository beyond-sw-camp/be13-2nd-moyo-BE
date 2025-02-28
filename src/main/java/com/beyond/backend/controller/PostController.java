package com.beyond.backend.controller;

import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.SearchOption;
import com.beyond.backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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

    @Operation(summary = "게시글 검색", description = "제목, 내용, 작성자에서 검색어가 포함된 게시글 조회")
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam BoardType boardType,
            @RequestParam(required = false) SearchOption option,
            @RequestParam String keyword,
            @PageableDefault(size = 10, page= 0)Pageable pageable) {
        Page<PostResponseDto> result = postService.searchPosts(boardType, option, keyword, pageable);
        if (result.isEmpty()) {
            return ResponseEntity.ok("게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글 id로 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        if (post == null) {
            return ResponseEntity.ok("해당 게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(post);
    }



}
