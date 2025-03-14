package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.CommentSortOption;
import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.common.exception.BaseException;
import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.AuthException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.post.dto.PostDto;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.PostWithCommentsResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.bookMark.entity.BookMark;
import com.beyond.backend.domain.bookMark.entity.BookMarkNo;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostSearchOption;
import com.beyond.backend.domain.post.entity.PostSortOption;
import com.beyond.backend.domain.post.entity.PostStatus;
import com.beyond.backend.domain.bookMark.repository.BookMarkRepository;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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



    @Override
    public Page<PostResponseDto> getPosts(BoardType boardType, Pageable pageable, PostSortOption postSortOption) {

        Page<PostResponseDto> allPosts = postRepository.getPostsByBoardType(boardType, pageable, postSortOption);

        // 게시글이 없는 경우 빈 목록을 전달, 컨트롤러에서 처리
        return allPosts;
    }

    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, PostSortOption postSortOption, String keyword, Pageable pageable) {

        if (keyword != null && option == null) {
            throw new IllegalArgumentException("검색 옵션을 선택해주십시오.");
        }

        Page<PostResponseDto> searchResults = postRepository.searchPosts(boardType, option, keyword, pageable, postSortOption);

        if (searchResults.isEmpty()) {
            throw new PostException(ExceptionMessage.POST_NOT_FOUND);
        }

        return searchResults;
    }

    // 게시글 단 건 조회
   /* @Override
    public PostResponseDto getPostById(Long postNo) {
        Post prePost = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));


        // 비활성화된 게시글인 경우 상세 조회 불가능
        if(prePost.getPostStatus() == PostStatus.INACTIVE){
            throw new IllegalArgumentException("해당 게시글은 비활성된 게시글로 볼 수 없습니다.");
        }

        // 조회수 증가
        postRepository.increaseViewCount(postNo);

        // 최신 게시글 재조회 (정합성 보장) - 이렇게 해야 게시글 조회수가 올라간 상태로 보임
        Post post = postRepository.findById(postNo)  
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (post.getPostStatus() == PostStatus.INACTIVE) {
            throw new IllegalArgumentException("해당 게시글은 비활성된 게시글로 볼 수 없습니다.");
        }

        // 최신 댓글 개수 조회 (정합성 보장)
 *//*       int latestCommentCount = postRepository.getLatestCommentCount(postNo);*//*

        // 게시글 단 건 조회 중간에 댓글이 삭제된 경우 최신 댓글 개수가 조회될 필요가 있나?

        return new PostResponseDto(post*//*, latestCommentCount*//*);
    }
*/
    // 게시글 단 건 조회와 댓글 가져오기
    // 게시글 비활성화해도 댓글은 조회 가능?
    // - 게시글 하나의 전체 댓글 조회 API를 요청하게 할까
    @Override
    public PostWithCommentsResponseDto getPostWithCommentsById(Long postNo, CommentSortOption commentSortOption, Pageable pageable) {


        Post prePost = postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        // 비활성화된 게시글인 경우 작성자가 아니면 댓글만 볼 수 있음
        if (prePost.getPostStatus() == PostStatus.INACTIVE) {
            throw new PostException(ExceptionMessage.POST_ACCESS_DENIED);
        }

        // 조회수 증가
        postRepository.increaseViewCount(postNo);

        // 최신 게시글 재조회 (정합성 보장) - 이렇게 해야 게시글 조회수가 올라간 상태로 보임
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        if (post.getPostStatus() == PostStatus.INACTIVE) {
            throw new PostException(ExceptionMessage.POST_ACCESS_DENIED);
            //, "해당 게시글은 비활성화된 상태입니다."
        }

        PostResponseDto postResponseDto = new PostResponseDto(post);

        // 댓글 조회
        Page<CommentResponseDto> commentsPage = commentRepository.getPostComments(postNo, commentSortOption, pageable);

        // 댓글 리스트
        List<CommentResponseDto> comments = commentsPage.getContent();

        return new PostWithCommentsResponseDto(postResponseDto, comments);
    }
    // 게시글 생성
    @Override
    @Transactional
    public PostResponseDto createPost(BoardType boardType, PostDto postDto) {


        // 유저가 존재하는 지 확인
        CustomUserDetails userDetails = authService.getCurrentUser();

        User user = userRepository.findById(userDetails.getUser().getNo())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userDetails.getUser().getNo()));

        // DTO -> entity 로 변환
        Post post = Post.builder()
                .postTitle(postDto.getTitle())
                .postContent(postDto.getContent())
                .boardType(boardType)
                .user(user)
                .postStatus(PostStatus.ACTIVE)
                .build();

        // repository 에 entity 저장
        postRepository.save(post);

        // entity -> responseDto 로 변환 후 반환
        return new PostResponseDto(post);

    }



    // 게시글 수정 
    // 게시글 번호와 게시글 데이터 받아서
    // 게시글 번호에 해당하는 게시글을 찾고
    // 업데이트된 게시글 dto 객체로 반환
    @Override
    @Transactional
    public PostResponseDto updatePost(BoardType boardType, PostStatus postStatus, Long postNo, PostDto postDto) {
        CustomUserDetails userDetails = authService.getCurrentUser();


        // 게시글 찾기
        Post post= postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        // 수정하려는 유저 찾기
        User user = userRepository.findById(post.getUser().getNo())
                .orElseThrow(()-> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userDetails.getUser().getNo()));

         //post의 유저와 수정하려는 유저가 같은지 확인

        post.update(postDto.getTitle(), postDto.getContent(), postDto.getPostStatus());


        return new PostResponseDto(post);
    }

    // 게시글과 댓글 연관관계 메서드 사용 생각해보기
    @Override
    @Transactional
    public void deletePost(Long postNo){

        CustomUserDetails userDetails = authService.getCurrentUser();

        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        // 관리자
//     if(!authService.validateAdminAuthorization() && !authService.validateUser(userDetails.getUser())){
//          throw new IllegalArgumentException("삭제할 권한이 없습니다.");
//        };


        postRepository.deleteById(postNo);

    }

    
    // 유저가 작성한 게시글 전체 조회 가능 ( boardtype,postsatus 알려줌)
    @Override
    public Page<UserPostResponseDto> getUserPosts(Pageable pageable) {

        CustomUserDetails userDetails = authService.getCurrentUser();

        // 유저가 존재하는지 확인
        User user = userRepository.findById(userDetails.getUser().getNo())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userDetails.getUser().getNo()));

        // 유저가 작성한 게시글 조회
        Page<UserPostResponseDto> userPosts = postRepository.getUserPosts(userDetails.getUser().getNo(), pageable);

        if (userPosts.isEmpty()) {
            throw new PostException(ExceptionMessage.POST_NOT_FOUND);
        }

        return userPosts;
    }



    private boolean isPostOwner(CustomUserDetails userDetails, Post post) {
        return userDetails.getUser().getNo().equals(post.getUser().getNo());
    }

}