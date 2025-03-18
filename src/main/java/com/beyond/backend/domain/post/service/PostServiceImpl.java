package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.comment.entity.CommentSortOption;
import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.post.dto.PostDto;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostSearchOption;
import com.beyond.backend.domain.post.entity.PostSortOption;
import com.beyond.backend.domain.post.entity.PostStatus;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.service.impl
 * <p>fileName       : PostServiceImpl
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;


    // 게시글 검색
    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, PostSortOption postSortOption, String keyword, Pageable pageable) {

        // 검색어가 있는데 검색 옵션을 선택하지 않은 경우 검색이 안됨
        if (keyword != null && option == null) {
            throw new IllegalArgumentException("검색 옵션을 선택해주십시오.");
        }
        
        Page<PostResponseDto> searchResults = postRepository.searchPosts(boardType, option, keyword, pageable, postSortOption);

        // 검색 결과가 없는 경우 예외 처리
        if (searchResults.isEmpty()) {
            throw new PostException(ExceptionMessage.POST_NOT_FOUND);
        }

        return searchResults;
    }



    // 게시글 단 건 조회(게시글 비활성화해도 관리자와 작성자는 조회 가능
    @Override
    public PostResponseDto getPostById(Long postNo) {

        // 로그인한 유저 정보 가져오기
        CustomUserDetails userDetails = authService.getCurrentUser();

        // 게시글이 존재하는지 확인
        Post prePost = postRepository.findByIdWithUser(postNo) //
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        // 비활성화된 게시글 처리
        if (prePost.getPostStatus() == PostStatus.INACTIVE) {
//authService.isUser(post.getUser())
            // 관리자나 작성자가 아닌 경우 예외 처리
            if (!authService.isAdmin() && !prePost.getUser().equals(userDetails.getUser())) {
                throw new PostException(ExceptionMessage.POST_ACCESS_DENIED);
            }

            // 비활성 게시글은 조회수 증가 제외( 최신 댓글 수만 조회하고 바로 돌려줌 )
            int latestCommentCount = postRepository.getLatestCommentCount(postNo);

            return new PostResponseDto(prePost, latestCommentCount);
        }
        // 활성화된 게시글인 경우

        // 조회수 증가
        postRepository.increaseViewCount(postNo);

        // 최신 댓글 개수 조회 (정합성 보장)
        int latestCommentCount = postRepository.getLatestCommentCount(postNo);

        Post post= postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        return new PostResponseDto(post, latestCommentCount);
    }



    // 게시글 생성
    @Override
    @Transactional
    public PostResponseDto createPost(BoardType boardType, PostDto postDto) {

        if (boardType == null) {
            throw new IllegalArgumentException("게시판 타입을 지정해 주십시오.");
        }

        // 공지 게시판을 선택한 경우 관리자인지 검증하는 서비스 호출 (일반 유저면 권한이 없음)


        // 로그인한 유저 정보 가져옴
        CustomUserDetails userDetails = authService.getCurrentUser();


        // DTO -> entity 로 변환
        Post post = Post.builder()
                .postTitle(postDto.getTitle())
                .postContent(postDto.getContent())
                .boardType(boardType)
                .user(userDetails.getUser())
                .postStatus(PostStatus.ACTIVE)
                .build();

        // repository 에 entity 저장
        postRepository.save(post);

        // entity -> responseDto 로 변환 후 반환
        return new PostResponseDto(post);

    }



    // 게시글 수정 // 보드 타입 수정 불가
    @Override
    @Transactional
    public PostResponseDto updatePost(PostStatus postStatus, Long postNo, PostDto postDto) {

        // 로그인한 유저 정보 가져옴
        CustomUserDetails userDetails = authService.getCurrentUser();

        // 게시글 찾기
        Post post= postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        // 게시글을 작성한 유저가 로그인한 유저와 같은지 검증

        authService.validateUser(post.getUser());


        // 게시글 수정
        post.update(postDto.getTitle(), postDto.getContent(), postDto.getPostStatus());
        
        return new PostResponseDto(post);
    }


    // 게시글 삭제
    @Override
    @Transactional
    public void deletePost(Long postNo){

        // 로그인한 유저 정보 가져옴
        CustomUserDetails userDetails = authService.getCurrentUser();

        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        // 게시글 작성자이거나 관리자인 경우만 삭제 가능

        if ( !authService.isUser(post.getUser()) && !authService.isAdmin()) {

            throw new PostException(ExceptionMessage.POST_ACCESS_DENIED);
        }

        postRepository.deleteById(postNo);

    }

    
    // 유저가 작성한 게시글 전체 조회 가능 (게시글 상태 알려줌)
    @Override
    public Page<UserPostResponseDto> getUserPosts(BoardType boardType, Pageable pageable) {

        //로그인한 유저 정보 가져옴
        CustomUserDetails userDetails = authService.getCurrentUser();

        // 유저가 존재하는지 확인
        userRepository.findById(userDetails.getUser().getNo())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userDetails.getUser().getNo()));

        // 유저가 작성한 게시글 조회
        Page<UserPostResponseDto> userPosts = postRepository.getUserPosts(userDetails.getUser().getNo(), boardType, pageable);

        // 유저가 작성한 게시글이 없는 경우 예외 처리
        if (userPosts.isEmpty()) {
            throw new PostException(ExceptionMessage.POST_NOT_FOUND);
        }

        return userPosts;
    }


    @Override
    @Transactional(readOnly = true)
    public void validatePostAuthority(BoardType boardType) {
        if (boardType == BoardType.NOTICE && !authService.isAdmin()) {
            throw new IllegalArgumentException("제시글 작성 권한이 없습니다.");
        }
    }

}