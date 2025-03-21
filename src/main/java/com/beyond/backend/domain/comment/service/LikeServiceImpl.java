package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.comment.entity.Like;
import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.comment.repository.LikeRepository;
import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    @Transactional
    public String toggleCommentLike(Long commentNo) {
        CustomUserDetails userDetails = authService.getCurrentUser();
        Long userId = userDetails.getNo(); // 현재 유저 아이디 가져오기
        String redisKey = "likes:" + commentNo; // Redis Key 생성
        String userKey = "user:" + userId;     // Redis User Key 생성
        String message = "";

        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(redisKey, userKey))) {
            // 이미 좋아요 한 상태인 경우 -> 좋아요 취소
            commentRepository.decreaseLikeCount(commentNo); // 좋아요 수 감소
            redisTemplate.opsForSet().remove(redisKey, userKey); // Redis에서 유저 정보 제거
            log.info("User {} unliked comment {}", userId, commentNo);
            message = "좋아요 취소 성공적";
        } else {
            // 좋아요하지 않은 상태인 경우 -> 좋아요 추가
            commentRepository.increaseLikeCount(commentNo); // 좋아요 수 증가
            redisTemplate.opsForSet().add(redisKey, userKey); // Redis에 유저 정보 추가
            log.info("User {} liked comment {}", userId, commentNo);
            message = "좋아요 성공적";


    // 좋아요 수 확인
    @Override
    public Long getLikeCount(Long commentNo) {
        String redisKey = "likes:" + commentNo;
        return redisTemplate.opsForSet().size(redisKey);
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
