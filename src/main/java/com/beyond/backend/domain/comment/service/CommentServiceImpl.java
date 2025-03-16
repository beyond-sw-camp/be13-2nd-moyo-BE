package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.comment.dto.CommentDto;
import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.comment.entity.CommentSortOption;
import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.entity.UserStatus;
import com.beyond.backend.domain.common.exception.BaseException;
import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.comment.repository.LikeRepository;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.Post;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {


    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public CommentResponseDto createComment(CommentDto commentDto, Long userNo) {

        CustomUserDetails userDetails = authService.getCurrentUser();
        // 게시글이 존재하는지 확인
        Post post = postRepository.findById(commentDto.getPostNo())
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + commentDto.getPostNo())
                );

        // 비활성화된 게시글은 애초에 상세 조회가 안됨 == 댓글도 못 씀

        // postNo을 넘겨받아서 게시글의 타입이 Free인지 확인

        if (post.getBoardType() != BoardType.FREE) {
            throw new IllegalArgumentException("자유게시판의 게시글 이외에는 댓글을 작성할 수 없습니다.");
        }


        // 게시글 비활성 상태면 댓글 작성 막음 아무도 댓글 못 씀
        if (post.getPostStatus() == PostStatus.INACTIVE) {
            throw new PostException(ExceptionMessage.POST_ACCESS_DENIED);
            //"비활성화된 게시글에는 댓글을 작성할 수 없습니다."
        }

        // 댓글 작성자 확인 및 활성화 여부 체크
        User sender = userRepository.findById(userNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo));


        if (sender.getUserStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("비활성화 상태거나 삭제된 회원은 댓글을 달 수 없습니다.");
        }

        if (!sender.getNo().equals(userDetails.getUser().getNo())) {
            throw new BaseException(ExceptionMessage.INVALID_REQUEST); // bad request
        }


        // 댓글 저장
        Comment comment = new Comment(commentDto.getContent(), post, sender);
        commentRepository.save(comment);

        // 댓글 개수 증가 (오류 처리 추가)
        int updatedCount = postRepository.increaseCommentCount(post.getNo());

        int latestCommentCount = postRepository.getLatestCommentCount(post.getNo());


        User receiver = post.getUser();

        // 트랜잭션 종료 후가 아니라, 바로 알림 전송
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
    @Transactional
    public CommentResponseDto updateComment(Long commentNo, CommentDto commentDto, Long userNo) {

        CustomUserDetails userDetails = authService.getCurrentUser();

        // 게시글이 존재하는지 확인
        Post post  = postRepository.findById(commentDto.getPostNo())
                .orElseThrow(()-> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + commentDto.getPostNo()));

        // 댓글이 존재하는 지 확인
        Comment comment  = commentRepository.findById(commentNo)
                .orElseThrow(()-> new PostException(ExceptionMessage.COMMENT_NOT_FOUND));



        User user = userRepository.findById(comment.getUser().getNo())
                .orElseThrow(()-> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + comment.getUser().getNo()));

        // 댓글 작성자가 로그인한 회원과 같은지 비교 userNo로
        // 댓글을 수정하려는 유저가 댓글을 작성한 유저인지 확인
        authService.validateUser(user);

        // 댓글 수정 (update 메서드 사용)
        comment.update(commentDto.getContent());

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long commentNo, Long userNo) {
        // 게시글이 지워지면 댓글도 같이 지워지는 것 생각
        CustomUserDetails userDetails = authService.getCurrentUser();

        // 댓글이 존재하는 지 검증
        Comment comment  = commentRepository.findById(commentNo)
                .orElseThrow(()-> new PostException(ExceptionMessage.COMMENT_NOT_FOUND));

        // 유저가 존재라는 지 검증
        User user = userRepository.findById(comment.getUser().getNo())
                .orElseThrow(()-> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + comment.getUser().getNo()));

        authService.validateUser(user);

        Post post = comment.getPost();
        // 댓글 삭제
        commentRepository.deleteById(commentNo); // 일단은 그냥 삭제

        // 댓글 개수 감소
        int updatedCount = postRepository.decreaseCommentCount(post.getNo());

        int latestCommentCount = postRepository.getLatestCommentCount(post.getNo());



    }



    // 로그인한 유저가 작성한 댓글 마이페이지에서 전체 조회 가능 ( 페이징 )
    @Override
    public Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable) {

        Page<CommentResponseDto> userComments = commentRepository.getUserComments(userNo, pageable);

        if (userComments.isEmpty()) {
            throw new PostException(ExceptionMessage.COMMENT_NOT_FOUND);
        }


        return userComments;
    }


    // 게시글의 댓글 전체 조회
    @Override
    public Page<CommentResponseDto> getPostComments(Long postNo, CommentSortOption commentSortOption, Pageable pageable) {

        // 게시글 조회
        Post post = postRepository.findById(postNo)
                .orElseThrow(() ->new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));


        if (post.getBoardType() != BoardType.FREE) {
            throw new IllegalArgumentException("해당 게시판에서는 댓글을 조회할 수 없습니다.");
        }

        Page<CommentResponseDto> postComment = commentRepository.getPostComments(postNo, commentSortOption, pageable);

        if( postComment.isEmpty()){
            throw new PostException(ExceptionMessage.COMMENT_NOT_FOUND);
        }

        return postComment;
    }

    // 유저가 작성한 댓글이 있는 게시글 전체 조회
    @Override
    public Page<PostResponseDto> getUserCommentPosts(Long userNo, Pageable pageable) {


        Page<PostResponseDto> commentPost = commentRepository.getUserCommentPosts(userNo, pageable);

        if (commentPost.isEmpty()) {
            throw new PostException(ExceptionMessage.POST_NOT_FOUND);
        }
        return commentPost;
    }







}
