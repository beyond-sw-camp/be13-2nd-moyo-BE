package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.post.dto.PostDto;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.bookMark.entity.BookMark;
import com.beyond.backend.domain.bookMark.entity.BookMarkNo;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostSearchOption;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BookMarkRepository bookMarkRepository;
    private final UserRepository userRepository;
    private final AuthService authService;



    @Override
    public Page<PostResponseDto> getPosts(BoardType boardType, Pageable pageable) {
        return postRepository.getPostsByBoardType(boardType, pageable);
    }

    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, String keyword, Pageable pageable) {
        return postRepository.searchPosts(boardType, option, keyword, pageable);
    }

    // 게시글 단 건 조회
    @Override
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
 /*       int latestCommentCount = postRepository.getLatestCommentCount(postNo);*/

        // 게시글 단 건 조회 중간에 댓글이 삭제된 경우 최신 댓글 개수가 조회될 필요가 있나?

        return new PostResponseDto(post/*, latestCommentCount*/);
    }

    // 게시글 생성
    @Override
    public PostResponseDto createPost(BoardType boardType, PostDto postDto, Long userNo) {


        if(boardType == BoardType.NOTICE){
            throw new IllegalArgumentException("공지 게시판에는 게시글을 작성할 수 없습니다.");
        }
        // 유저가 존재하는 지 확인
        User user = userRepository.findById(userNo).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );

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



    // 게시글 수정 (오류남)
    // 게시글 번호와 게시글 데이터 받아서
    // 게시글 번호에 해당하는 게시글을 찾고
    // 업데이트된 게시글 dto 객체로 반환
    @Override
    public PostResponseDto updatePost(BoardType boardType, PostStatus postStatus, Long postNo, PostDto postDto, Long userNo) {
        CustomUserDetails userDetails = authService.getCurrentUser();

        if(boardType == BoardType.NOTICE){
            throw new IllegalArgumentException("공지 게시판의 게시글은 수정할 수 없습니다.");
        }

        // 게시글 찾기
        Post post= postRepository.findById(postNo)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        // 수정하려는 유저 찾기
        User user = userRepository.findById(userNo)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        if (!user.equals(userDetails.getUser()) || authService.isAdminFromUserDetails(userDetails)) {
            throw new IllegalArgumentException("bad request");
        }

        // post의 유저와 수정하려는 유저가 같은지 확인

        if (!user.equals(userDetails.getUser()) || authService.isAdminFromUserDetails(userDetails)) {
            throw new IllegalArgumentException("bad request");
        }



        return new PostResponseDto(post);
    }

    
    // 나중에 댓글 만들고 댓글도 같이 삭제 시키는 것 + 상태 변화 deleted로 만들기
    @Override
    public void deletePost(Long postNo){

     postRepository.deleteById(postNo);

    }

    
    // 유저가 작성한 게시글 전체 조회 가능 ( boardtype,postsatus 알려줌)
    @Override
    public Page<UserPostResponseDto> getUserPosts(Long userNo, Pageable pageable) {
        return postRepository.getUserPosts(userNo, pageable);
    }


    // 게시글 북마크
    @Override
    public String checkBookMark(Long postNo, Long userNo) {

        BookMarkNo bookMarkNo = new BookMarkNo(postNo, userNo);

        // 게시글 비활성화 상태이면 북마크 추가 불가능 ( 비활성 상태인 게시글은 전체 게시글에서 보이지 않음 )
        // 활성화 상태이던 게시글이 비활성화된 경우 내 북마크 리스트에서는 보이지만 게시글에 들어갈 수는 없고
        // 북마크 취소만 가능
        // 내가 북마크한 게시글 리스트 조회 시 비활성화된 게시글이 보임 상세 조회가 안됨

        // 게시글이 북마크된 게시글인지 찾고 있으면 삭제
        // 게시글 비활성화 상태이면 북마크 추가 불가능
        if (bookMarkRepository.existsById(bookMarkNo)) {
            bookMarkRepository.deleteById(bookMarkNo);

            int updatedCount = postRepository.decreaseBookmark(postNo);

            if (updatedCount == 0) {
                throw new IllegalStateException("게시글 북마크 개수가 0입니다.");
            }

            //  최신 북마크 수 조회 (정합성 문제 해결)
            int latestCount = postRepository.getLatestBookmarkCount(postNo);

            return "북마크가 해제되었습니다.";
        }

        // 신규 북마크 추가
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 새 북마크 생성 및 저장
        BookMark newBookMark = new BookMark(bookMarkNo, post, user);
        bookMarkRepository.save(newBookMark);

        int updatedCount = postRepository.increaseBookmark(postNo);

        if (updatedCount == 0) {
            throw new IllegalStateException("북마크 증가 실패: 게시글이 존재하지 않습니다.");
        }

        int latestCount = postRepository.getLatestBookmarkCount(postNo);

        return "북마크가 추가되었습니다.";
    }



    // 북마크된 게시글 전체 조회
    @Override
    public Page<UserPostResponseDto> getBookmarkedPosts(Long userNo, BoardType boardType, Pageable pageable) {
        return bookMarkRepository.getBookmarkedPosts(userNo, boardType, pageable);
    }
}