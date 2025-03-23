package com.beyond.backend.domain.feedback.service;

import java.util.Optional;

import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.ProjectException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.feedback.dto.FeedbackRequestDto;
import com.beyond.backend.domain.feedback.dto.FeedbackResponseDto;
import com.beyond.backend.domain.feedback.dto.FeedbackUpdateRequestDto;
import com.beyond.backend.domain.feedback.entity.Feedback;
import com.beyond.backend.domain.feedback.entity.FeedbackType;
import com.beyond.backend.domain.feedback.repository.FeedbackRepository;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.teamUser.repository.TeamUserRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class FeedbackServiceImpl implements FeedbackService {

	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final FeedbackRepository feedbackRepository;
	private final AuthService authService;
	private final TeamUserRepository teamUserRepository;

	// 1. feedback 팀원만 가능?? yes

	@Transactional
	public FeedbackResponseDto createFeedback(Long projectNo, FeedbackType feedbackType, FeedbackRequestDto feedbackDto) {

		// 로그인한 사용자 정보 가져오기
		User user = authService.getCurrentUser().getUser();

		// 1. 프로젝트가 존재하는지
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new ProjectException(ExceptionMessage.PROJECT_NOT_FOUND)
		);

		// 3. user 가 팀에 속하는지
		if (!teamUserRepository.existsByUserNoAndTeamNo(user.getNo(), project.getTeam().getNo())){
			throw new IllegalArgumentException("해당 프로젝트에 피드백을 작성할 권한이 없습니다.");
		}

		// 3. create
		Feedback feedback = Feedback.builder()
									.content(feedbackDto.getContent())
									.user(user)
									.project(project)
									.feedbackType(feedbackType)
									.build();

		feedbackRepository.save(feedback);

		return new FeedbackResponseDto(feedback);
	}

	@Transactional
	public FeedbackResponseDto updateFeedback(Long projectNo, Long feedbackNo, FeedbackType feedbackType, FeedbackUpdateRequestDto dto){

		User user = authService.getCurrentUser().getUser();
		// 2. 프로젝트 존재 여부
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new ProjectException(ExceptionMessage.PROJECT_NOT_FOUND)
		);

		// 3. user 가 팀에 속하는지
		if (!teamUserRepository.existsByUserNoAndTeamNo(user.getNo(), project.getTeam().getNo())){
			throw new IllegalArgumentException("해당 프로젝트에 피드백을 작성할 권한이 없습니다.");
		}

		// 4. 피드백 존재 여부
		Feedback feedback = feedbackRepository.findById(feedbackNo).orElseThrow(
			() -> new PostException(ExceptionMessage.FEEDBACK_NOT_FOUND)
		);

		// 5. 본인의 피드백인지 검증
		authService.validateUser(feedback.getUser());

		feedback.updateFeedback(dto.getContent(), feedbackType);

		return new FeedbackResponseDto(feedback);
	}



	@Transactional
	public void deleteFeedback(Long feedbackNo) {

		Feedback feedback = feedbackRepository.findById(feedbackNo).orElseThrow(
				() ->  new PostException(ExceptionMessage.FEEDBACK_NOT_FOUND)
		);

		if ( !authService.isAdmin() ) {
			authService.validateUser(feedback.getUser());
		}

		Long userNo = authService.getCurrentUser().getUser().getNo();

		if (!teamUserRepository.isLeader(feedback.getProject().getTeam().getNo(), userNo)) {
			throw new IllegalArgumentException("삭제할 권한이 없습니다.");
		}


		feedbackRepository.deleteById(feedbackNo);
	}
  

	@Override
	public Page<FeedbackResponseDto> getFeedbackByUserNo(Long userNo, Pageable pageable) {

		// 1. user가 존재하는지
		User user = userRepository.findById(userNo).orElseThrow(
			() -> new UserException(ExceptionMessage.USER_NOT_FOUND)
		);

		// 2. user의 feedback 가져오기
		Page<FeedbackResponseDto> feedbackList = feedbackRepository.findFeedbackByUserId(userNo, pageable);

		return feedbackList;
	}


	@Override
	public Page<FeedbackResponseDto> getFeedbackByProjectNo(Long userNo, Long projectNo, Pageable pageable) {

		// 1. 프로젝트가 존재하는지
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new ProjectException(ExceptionMessage.PROJECT_NOT_FOUND)
		);

		// 2. user 가 팀에 속하는지
		if (!teamUserRepository.existsByUserNoAndTeamNo(userNo, project.getTeam().getNo())){
			throw new IllegalArgumentException("해당 프로젝트에 피드백에 접근할 권한이 없습니다.");
		}

		// 3. project feedback 가져오기
		Page<FeedbackResponseDto> feedbackByProjectId = feedbackRepository.findFeedbackByProjectId(projectNo, pageable);

		return feedbackByProjectId;
	}

	@Override
	public Page<FeedbackResponseDto> getAllFeedback(Pageable pageable) {

		Page<FeedbackResponseDto> allFeedback = feedbackRepository.findAllFeedback(pageable);

		return allFeedback;
	}
}
