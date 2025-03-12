package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.comment.dto.CommentDto;
import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.CommentSortOption;
import com.beyond.backend.domain.like.entity.Like;
import com.beyond.backend.domain.like.repository.LikeRepository;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.entity.Status;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.comment.entity.Comment;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostStatus;
import com.beyond.backend.domain.comment.repository.CommentRepository;
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
        User sender = userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));


        if (sender.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("비활성화 상태거나 삭제된 회원은 댓글을 달 수 없습니다.");
        }

        if (!sender.getNo().equals(userDetails.getUser().getNo())) {
            throw new IllegalArgumentException("bad request");
        }


        // 댓글 저장
        Comment comment = new Comment(commentDto.getContent(), post, sender);
        commentRepository.save(comment);

        // 댓글 개수 증가 (오류 처리 추가)
        int updatedCount = postRepository.increaseCommentCount(post.getNo());

        int latestCommentCount = postRepository.getLatestCommentCount(post.getNo());


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
    @Transactional
    public CommentResponseDto updateComment(Long commentNo, CommentDto commentDto, Long userNo) {

        CustomUserDetails userDetails = authService.getCurrentUser();

        // 게시글이 존재하는지 확인
        Post post  = postRepository.findById(commentDto.getPostNo())
                .orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 댓글이 존재하는 지 확인
         Comment comment  = commentRepository.findById(commentNo)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));



        User user = userRepository.findById(comment.getUser().getNo())
                .orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 없습니다."));

        // 댓글 작성자가 로그인한 회원과 같은지 비교 userNo로
        // 댓글을 수정하려는 유저가 댓글을 작성한 유저인지 확인
        if (!user.getNo().equals(userDetails.getUser().getNo())) {
            throw new IllegalArgumentException("bad request");
        }


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
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        // 유저가 존재라는 지 검증
        User user = userRepository.findById(comment.getUser().getNo())
                .orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 없습니다."));

        //작성자와 로그인한 유저가 같은지 확인
        if (!user.getNo().equals(userDetails.getUser().getNo()) && !authService.isAdminFromUserDetails(userDetails)) {
            throw new IllegalArgumentException("bad request");
        }


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

        if( userComments.isEmpty()){
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }


        return userComments;
    }


    // 게시글의 댓글 전체 조회
    @Override
    public Page<CommentResponseDto> getPostComments(Long postNo, CommentSortOption commentSortOption, Pageable pageable) {

        // 게시글 조회
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));


        if (post.getBoardType() != BoardType.FREE) {
            throw new IllegalArgumentException("해당 게시판에서는 댓글을 조회할 수 없습니다.");
        }

        Page<CommentResponseDto> postComment = commentRepository.getPostComments(postNo, commentSortOption, pageable);

        if( postComment.isEmpty()){
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }

        return postComment;
    }

    // 유저가 작성한 댓글이 있는 게시글 전체 조회
    @Override
    public Page<PostResponseDto> getUserCommentPosts(Long userNo, Pageable pageable) {


        Page<PostResponseDto> commentPost = commentRepository.getUserCommentPosts(userNo, pageable);
        
        if (commentPost.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        return commentPost;
    }

    


    //-----------------------------------------------------------

    // 댓글 좋아요
    @Override
    @Transactional
    public String checkCommentLike(Long commentNo, Long userNo) {

        CustomUserDetails userDetails = authService.getCurrentUser();
        // 댓글이 존재하는지 확인
        Comment comment = commentRepository.findById(commentNo).orElseThrow(
                ()-> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 유저가 존재하는지 확인
        User user = userRepository.findById(userNo).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 로그인한 유저만 좋아요 가능
        if (!user.getNo().equals(userDetails.getUser().getNo())) {
            throw new IllegalArgumentException("bad request");
        }


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
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }

        return likedComment;
    }


}
