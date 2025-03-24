package com.beyond.backend.domain.common;

import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.ProjectException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.feedback.entity.Feedback;
import com.beyond.backend.domain.feedback.repository.FeedbackRepository;
import com.beyond.backend.domain.message.repository.MessageRepository;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostStatus;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.report.repository.ReportRepository;
import com.beyond.backend.domain.team.entity.TeamJoinStatus;
import com.beyond.backend.domain.team.repository.TeamRepository;
import com.beyond.backend.domain.teamUser.repository.TeamUserRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.common
 * <p>fileName       : CustomPermissionEvaluator
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 23.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 23.        hyunjo             최초 생성
 */
@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {


    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FeedbackRepository feedbackRepository;
    private final MessageRepository messageRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject == null) {
            return false;
        }

        Long resourceId = (Long) targetDomainObject;


        switch (permission.toString().toUpperCase()) {
            case "POST":
                return isPostOwner(resourceId, authentication);
            case "POST_ACCESS":
                return canAccessPost(resourceId, authentication);
            case "COMMENT":
                return isCommentOwner(resourceId, authentication);
            case "FEEDBACK":
                return isFeedbackOwner(resourceId, authentication);
            case "FEEDBACK_DELETE":
                return canDeleteFeedback(resourceId, authentication);
            case "MESSAGE":
                return isMessageOwner(resourceId, authentication);
            case "TEAM":
                return isTeamOwner(resourceId, authentication);
            case "TEAM_MEMBER":
                return isTeamMember(resourceId, authentication);  // 팀원 검증
            case "PROJECT_TEAM_LEADER":
                return isProjectTeamLeader(resourceId, authentication);
            case "PROJECT_TEAM_MEMBER":
                return isProjectTeamMember(resourceId, authentication); // 프로젝트에서 팀원 검증
            case "REPORT":
                return isReportOwner(resourceId, authentication);
            default:
                return false;
        }
    }

    // 게시글 작성자 검증
    private boolean isPostOwner(Long postNo, Authentication authentication) {
        return postRepository.findById(postNo)
                .map(post -> {
                    if (post.getPostStatus() == PostStatus.ACTIVE ||
                            post.getPostStatus() == PostStatus.INACTIVE) {
                        return isOwner(post.getUser(), authentication);
                    } else {
                        throw new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo);
                    }
                })
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));
    }

    // 비활성화된 게시글 작성자 접근 검증 ( 게시글 단 건 조회 시 사용 )

    private boolean canAccessPost(Long postNo, Authentication authentication) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new PostException(ExceptionMessage.POST_NOT_FOUND, "ID: " + postNo));

                        // 활성화된 게시글은 모두 접근 가능
        if (post.getPostStatus() == PostStatus.ACTIVE) {
            return true;
        }


        // 비활성화된 경우 작성자 접근 허용

        return isOwner(post.getUser(), authentication);
    }

    // 댓글 작성자 검증
    private boolean isCommentOwner(Long commentNo, Authentication authentication) {
        return commentRepository.findById(commentNo)
                .map(comment -> isOwner(comment.getUser(), authentication))
                .orElse(false);
    }


  // 피드백 작성자 검증
    private boolean isFeedbackOwner(Long feedbackNo, Authentication authentication) {
        Feedback feedback = feedbackRepository.findById(feedbackNo)
                .orElseThrow(() -> new ProjectException(ExceptionMessage.FEEDBACK_NOT_FOUND, "ID: " + feedbackNo));

        // isOwner() 메서드를 통해 작성자 검증
        return isOwner(feedback.getUser(), authentication);
    }


    // 피드백 삭제 권한 검증 (팀장만 가능)
    private boolean canDeleteFeedback(Long feedbackNo, Authentication authentication) {
        return feedbackRepository.findById(feedbackNo)
                .map(feedback -> {
                    Long projectNo = feedback.getProject().getNo(); // 피드백 → 프로젝트 ID 추출
                    return isProjectTeamLeader(projectNo, authentication); // 해당 프로젝트의 팀장 검증
                })
                .orElse(false);  // 피드백이 존재하지 않으면 false
    }
  
    // 쪽지 작성자 검증
    private boolean isMessageOwner(Long messageNo, Authentication authentication) {
        return messageRepository.findById(messageNo)
                .map(message -> isOwner(message.getSender(), authentication) ||
                        isOwner(message.getReceiver(), authentication))
                .orElse(false);
    }

   // **팀** 작성자 검증 (팀장 검증)
    private boolean isTeamOwner(Long teamNo, Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        Boolean isLeader = teamUserRepository.isLeader(teamNo, user.getNo());

        // 팀장이면 true, 아닐 경우 false 처리
        return Boolean.TRUE.equals(isLeader);  // NullPointerException 방지
    }

    // **프로젝트 팀장 검증 로직 추가**
    private boolean isProjectTeamLeader(Long projectNo, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }

        CustomUserDetails currentUser = (CustomUserDetails) principal;

        //  projectNo로 해당 프로젝트의 팀 번호 조회
        Project project = projectRepository.findById(projectNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없습니다."));

        Long teamNo = project.getTeam().getNo();  // 프로젝트의 팀 번호 가져오기

        //  팀 번호를 통해 해당 유저가 '팀장'인지 검증
        return Boolean.TRUE.equals(teamUserRepository.isLeader(teamNo, currentUser.getUser().getNo()));
    }


    //----------------------- ( teamNo으로 팀원 검증, projectNo으로 팀원 검증 )
    // **팀원** 검증
    private boolean isTeamMember(Long teamNo, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        return teamUserRepository.existsByUserNoAndTeamNo(teamNo, user.getNo());
    }

    // **프로젝트 팀원 검증 로직 추가**
    private boolean isProjectTeamMember(Long projectNo, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }

        CustomUserDetails currentUser = (CustomUserDetails) principal;

        //  projectNo로 해당 프로젝트의 팀 번호 조회
        Project project = projectRepository.findById(projectNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없습니다."));

        Long teamNo = project.getTeam().getNo();  // 프로젝트의 팀 번호 가져오기

        // 팀 번호를 통해 해당 유저가 팀원인지 확인
        return teamUserRepository.existsByUserNoAndTeamNo(teamNo, currentUser.getUser().getNo());
    }

    //---------------------------------

    // **신고** 작성자 검증
    private boolean isReportOwner(Long reportNo, Authentication authentication) {
        return reportRepository.findById(reportNo)
                .map(report -> isOwner(report.getReporter(), authentication))
                .orElse(false);
    }


    // 유저(작성자) 검증 로직

    private boolean isOwner(User user, Authentication authentication) {
        // 유저 또는 인증 객체가 비어있을 경우 false 반환
        if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(authentication)) {
            return false;
        }

        // 인증 객체에서 principal이 CustomUserDetails인지 확인
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }

        CustomUserDetails currentUser = (CustomUserDetails) principal;


        // 사용자 본인 여부 확인
        return user.getNo().equals(currentUser.getUser().getNo());
    }

    // 필요하지 않은 경우 false 반환
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
