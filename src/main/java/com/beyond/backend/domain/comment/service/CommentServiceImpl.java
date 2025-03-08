package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.bookMark.entity.BookMark;
import com.beyond.backend.domain.bookMark.entity.BookMarkNo;
import com.beyond.backend.domain.comment.dto.CommentDto;
import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.like.entity.Like;
import com.beyond.backend.domain.like.repository.LikeRepository;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.common.CustomTransactionSynchronization;
import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.Notification;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.entity.Status;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostStatus;
import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : CommentServiceImpl
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 4.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 4.        hyunjo             최초 생성
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {


    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDto createComment(CommentDto commentDto) {

        // 게시글이 존재하는지 확인
        Post post = postRepository.findById(commentDto.getPostNo())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 비활성화된 게시글은 애초에 상세 조회가 안됨 == 댓글도 못 씀

        // postNo을 넘겨받아서 게시글의 타입이 Free인지 확인

        if (post.getBoardType() != BoardType.FREE) {
            throw new IllegalArgumentException("자유게시판의 게시글 이외에는 댓글을 작성할 수 없습니다.");
        }


        // 게시글 비활성 상태면 댓글 작성 막음 아무도 댓글 못 씀
        if (post.getPostStatus() == PostStatus.INACTIVE) {
            throw new IllegalArgumentException("비활성화된 게시글에는 댓글을 작성할 수 없습니다.");
        }

        // 댓글 작성자 확인 및 활성화 여부 체크
        User sender = userRepository.findById(commentDto.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (sender.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("비활성화 상태거나 삭제된 회원은 댓글을 달 수 없습니다.");
        }

        // 댓글 저장
        Comment comment = new Comment(commentDto.getContent(), post, sender);
        commentRepository.save(comment);

        // 📌 올바른 방식으로 게시글 작성자 가져오기
        User receiver = post.getUser(); // post.getNo()가 아니라 post.getUser() 사용

        // 🚀 트랜잭션 종료 후가 아니라, 바로 알림 전송
        notificationService.sendNotification(
                new RequestNotificationDto(
                        sender.getUsername(),
                        receiver.getUsername(),
                        NotificationType.COMMENT,
                        "새 댓글 등록 완료")
        );


        // entity -> responseDto 로 변환 후 반환
        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    @Override
    public CommentResponseDto updateComment(Long commentNo, CommentDto commentDto) {

        // 게시글이 존재하는지 확인
        Post post  = postRepository.findById(commentDto.getPostNo())
                .orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 댓글이 존재하는 지 확인
         Comment comment  = commentRepository.findById(commentNo)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));


        // 유저가 존재하는지 확인
        User user = userRepository.findById(commentDto.getUserNo())
                .orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 없습니다."));

        // 댓글 작성자가 로그인한 회원과 같은지 비교 userNo로

        // 댓글 수정 (update 메서드 사용)
        comment.update(commentDto.getContent());

        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long commentNo, Long userNo) {
        // 게시글이 지워지면 댓글도 같이 지워져서 생갛

        // 댓글이 존재하는 지 검증
        Comment comment  = commentRepository.findById(commentNo)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        // 유저가 존재라는 지 검증
        User user = userRepository.findById(userNo)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 없습니다."));
        //작성자와 로그인한 유저가 같은지 확인
        
        // 댓글 삭제
        commentRepository.deleteById(commentNo); // 일단은 그냥 삭제

    }



    // 로그인한 유저가 작성한 댓글 마이페이지에서 전체 조회 가능 ( 페이징 )
    @Override
    public Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable) {

        return commentRepository.getUserComments(userNo, pageable);
    }


    // 게시글의 댓글 전체 조회
    @Override
    public Page<CommentResponseDto> getPostComments(Long postNo, Pageable pageable) {
        return commentRepository.getPostComments(postNo, pageable);
    }

    // 유저가 작성한 댓글이 있는 게시글 전체 조회
    @Override
    public Page<PostResponseDto> getUserCommentPosts(Long userNo, Pageable pageable) {
        return commentRepository.getUserCommentPosts(userNo, pageable);
    }

    


    //-----------------------------------------------------------

    // 댓글 좋아요
    @Override
    public String checkCommentLike(Long commentNo, Long userNo) {

        // 댓글이 존재하는지 확인
        Comment comment = commentRepository.findById(commentNo).orElseThrow(
                ()-> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 유저가 존재하는지 확인
        User user = userRepository.findById(userNo).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));



        // 좋아요가 되어있는지 확인
        Like existingLike = likeRepository.findByCommentAndUser(comment, user);

        if (existingLike != null) {
            // 좋아요가 이미 있다면 삭제 (좋아요 취소)
            likeRepository.delete(existingLike);
            return "좋아요가 취소되었습니다.";
        }

        // 4. 좋아요 추가
        Like newLike = new Like( comment, user);
        likeRepository.save(newLike);

        return "좋아요가 추가되었습니다.";
    }

    // 유저가 좋아요한 댓글 전체 조회
    @Override
    public Page<CommentResponseDto> getUserLikedComments(Long userNo, Pageable pageable) {
        return likeRepository.getUserLikedComments(userNo,pageable);
    }


}
