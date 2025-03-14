package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.exception.BaseException;
import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.like.entity.Like;
import com.beyond.backend.domain.like.repository.LikeRepository;
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
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuthService authService;


    // 댓글 좋아요
    @Override
    @Transactional
    public String checkCommentLike(Long commentNo, Long userNo) {

        CustomUserDetails userDetails = authService.getCurrentUser();
        // 댓글이 존재하는지 확인
        Comment comment = commentRepository.findById(commentNo).orElseThrow(
                ()-> new PostException(ExceptionMessage.COMMENT_NOT_FOUND));

        // 유저가 존재하는지 확인
        User user = userRepository.findById(userNo).orElseThrow(
                ()-> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo));

        // 로그인한 유저만 좋아요 가능
        authService.validateUser(user);


        // 좋아요가 되어있는지 확인
        Like existingLike = likeRepository.findByCommentAndUser(comment, user);

        if (existingLike != null) {
            // 좋아요가 이미 있다면 삭제 (좋아요 취소)
            likeRepository.delete(existingLike);

            // 좋아요 개수 -1
            int updatedCount = commentRepository.decreaseLikeCount(commentNo);

            // 좋아요 개수가 0이면 좋아요 취소 실패
            if (updatedCount == 0) {
                throw new IllegalStateException("좋아요 취소 실패(이미 좋아요 개수가 0입니다.)");
            }

            // 최신 좋아요 개수 반환
            int latestCount = commentRepository.getLatestLikeCount(commentNo);

            return "좋아요가 취소되었습니다.";
        }

        // 4. 좋아요 추가
        Like newLike = new Like( comment, user);
        likeRepository.save(newLike);

        //좋아요 알림///////////////////////////
        User receiver = comment.getUser();

        // 트랜잭션 종료 후가 아니라, 바로 알림 전송
        notificationService.sendNotification(
                new RequestNotificationDto(
                        user.getUsername(),
                        receiver.getUsername(),
                        NotificationType.COMMENT,
                        user.getUsername() + "가 님의 게시글에 좋아요 누름")
        );


        // 좋아요 개수 +1
        int updatedCount = commentRepository.increaseLikeCount(commentNo);

        if (updatedCount == 0) {
            throw new IllegalStateException("좋아요 추가 실패: 댓글이 존재하지 않습니다.");
        }


        // 최신 좋아요 개수 반환
        int latestCount = commentRepository.getLatestLikeCount(commentNo);

        return "좋아요가 추가되었습니다.";
    }

    // 유저가 좋아요한 댓글 전체 조회
    @Override
    public Page<CommentResponseDto> getUserLikedComments(Long userNo, Pageable pageable) {

        Page<CommentResponseDto> likedComment = likeRepository.getUserLikedComments(userNo,pageable);


        if (likedComment.isEmpty()) {
            throw new PostException(ExceptionMessage.COMMENT_NOT_FOUND);
        }

        return likedComment;
    }
}
