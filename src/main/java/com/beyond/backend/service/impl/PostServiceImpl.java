package com.beyond.backend.service.impl;

import com.beyond.backend.data.dto.postDto.PostDto;
import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.dto.postDto.UserPostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.BookMark;
import com.beyond.backend.data.entity.BookMarkNo;
import com.beyond.backend.data.entity.Post;
import com.beyond.backend.data.entity.PostSearchOption;
import com.beyond.backend.data.entity.PostStatus;
import com.beyond.backend.data.entity.User;
import com.beyond.backend.data.repository.BookMarkRepository;
import com.beyond.backend.data.repository.PostRepository;
import com.beyond.backend.data.repository.UserRepository;
import com.beyond.backend.service.PostService;
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


    @Override
    public Page<PostResponseDto> getPosts(BoardType boardType, Pageable pageable) {
        return postRepository.getPostsByBoardType(boardType, pageable);
    }

    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, String keyword, Pageable pageable) {
        return postRepository.searchPosts(boardType, option, keyword, pageable);
    }

    @Override
    public PostResponseDto getPostById(Long postNo) {
        Post foundPost = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));


        // 비활성화된 게시글인 경우 상세 조회 불가능
        if(foundPost.getPostStatus() == PostStatus.INACTIVE){
            throw new IllegalArgumentException("해당 게시글은 비활성된 게시글로 볼 수 없습니다.");
        }
        return new PostResponseDto(foundPost);
    }

    // 게시글 생성
    @Override
    public PostResponseDto createPost(BoardType boardType, PostDto postDto) {


        if(boardType == BoardType.NOTICE){
            throw new IllegalArgumentException("공지 게시판에는 게시글을 작성할 수 없습니다.");
        }
        // 유저가 존재하는 지 확인
        User user = userRepository.findById(postDto.getUserNo()).orElseThrow(
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



    // 게시글 수정
    // 게시글 번호와 게시글 데이터 받아서
    // 게시글 번호에 해당하는 게시글을 찾고
    // 업데이트된 게시글 dto 객체로 반환
    @Override
    public PostResponseDto updatePost(BoardType boardType, PostStatus postStatus, Long postNo, PostDto postDto) {

        if(boardType == BoardType.NOTICE){
            throw new IllegalArgumentException("공지 게시판의 게시글은 수정할 수 없습니다.");
        }

        // 게시글 찾기
        Post post= postRepository.findById(postNo)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        // 수정하려는 유저 찾기
        User user = userRepository.findById(postDto.getUserNo())
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));



        post.update(postDto.getTitle(), postDto.getContent(), postDto.getPostStatus());


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


    //------------------------------------------------------------

    // 게시글 북마크
    @Override
    public String checkBookMark(Long postNo, Long userNo) {

        BookMarkNo bookMarkNo = new BookMarkNo(postNo, userNo);

        // 게시글 비활성화 상태이면 북마크 추가 불가능 ( 비활성 상태인 게시글은 전체 게시글에서 보이지 않음 )
        // 활성화 상태이던 게시글이 비활성화된 경우 내 북마크 리스트에서는 보이지만 게시글에 들어갈 수는 없고 
        // 북마크 취소만 가능
        // 내가 북마크한 게시글 리스트 조회 시 비활성화된 게시글이 보임 상세 조회가 안됨


        // 게시글이 북마크된 게시글인지 찾고 있으면 삭제
        if (bookMarkRepository.existsById(bookMarkNo)) {

            BookMark existingBookMark = bookMarkRepository.findById(bookMarkNo)
                    .orElseThrow(() -> new RuntimeException("북마크가 존재하지 않습니다."));

            bookMarkRepository.delete(existingBookMark);
            return "북마크가 해제되었습니다.";
        }


        // 게시글이랑 유저 존재하는 지 확인
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 새 북마크 생성 저장
        BookMark newBookMark = new BookMark(bookMarkNo, post, user);
        bookMarkRepository.save(newBookMark);

        return "북마크가 추가되었습니다.";
    }


    // 북마크된 게시글 전체 조회
    @Override
    public Page<UserPostResponseDto> getBookmarkedPosts(Long userNo, BoardType boardType, Pageable pageable) {
        return bookMarkRepository.getBookmarkedPosts(userNo, boardType, pageable);
    }



}
