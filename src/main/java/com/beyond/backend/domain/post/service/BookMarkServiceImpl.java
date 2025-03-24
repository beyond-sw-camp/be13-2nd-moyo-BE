package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.post.entity.BookMark;
import com.beyond.backend.domain.post.entity.BookMarkNo;
import com.beyond.backend.domain.post.repository.BookMarkRepository;
import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.Post;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkServiceImpl implements BookMarkService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;

    private final AuthService authService;


    // 게시글 북마크
    @Override
    @Transactional
    public String checkBookMark(Long postNo, Long userNo) {

        //CustomUserDetails userDetails = authService.getCurrentUser();

        // 로그인한 유저인지 검증
        //authService.validateUser(userDetails.getUser());

        BookMarkNo bookMarkNo = new BookMarkNo(postNo,userNo);

        // 게시글 비활성화 상태이면 북마크 추가 불가능 ( 비활성 상태인 게시글은 전체 게시글에서 보이지 않음 )
        // 내 게시글이어도 북마크 추가 못함

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
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo));

        // 로그인한 유저만 북마크 가능
        if (!user.getNo().equals(userNo)) {
            throw new UserException(ExceptionMessage.USER_ACCESS_DENIED);
        }

        // 새 북마크 생성 및 저장
        BookMark newBookMark = new BookMark(bookMarkNo, post, user);
        bookMarkRepository.save(newBookMark);

        int updatedCount = postRepository.increaseBookmark(postNo);

        if (updatedCount == 0) {
            throw new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo);
        }

        int latestCount = postRepository.getLatestBookmarkCount(postNo);

        return "북마크가 추가되었습니다.";
    }



    // 북마크된 게시글 전체 조회
    @Override
    public Page<UserPostResponseDto> getBookmarkedPosts(BoardType boardType, Pageable pageable, Long userNo) {

        //CustomUserDetails userDetails = authService.getCurrentUser();
        // 유저 존재 확인
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo));

        // 북마크한 게시글 조회
        Page<UserPostResponseDto> bookmarkedPosts = bookMarkRepository.getBookmarkedPosts(userNo, boardType, pageable);

        if (bookmarkedPosts.isEmpty()) {
            throw new PostException(ExceptionMessage.POST_NOT_FOUND);
        }

        return bookmarkedPosts;
    }
}
