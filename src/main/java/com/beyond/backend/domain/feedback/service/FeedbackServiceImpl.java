package com.beyond.backend.domain.feedback.service;

import com.beyond.backend.domain.feedback.entity.FeedbackType;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beyond.backend.domain.feedback.dto.FeedbackRequestDto;
import com.beyond.backend.domain.feedback.dto.FeedbackResponseDto;
import com.beyond.backend.domain.feedback.dto.FeedbackUpdateRequestDto;
import com.beyond.backend.domain.feedback.entity.Feedback;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.team.entity.Team;
import com.beyond.backend.domain.teamUser.entity.TeamUser;
import com.beyond.backend.domain.feedback.repository.FeedbackRepository;
import com.beyond.backend.domain.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final FeedbackRepository feedbackRepository;

	// 1. feedback 팀원만 가능?? yes
	// 2. feedback 회고록은 각 팀원당 한번만 가능
	// 3. 그 외의 것은 게시판 그잡채

	@Transactional
	public FeedbackResponseDto createFeedback(Long userNo, Long projectNo, FeedbackType feedbackType, FeedbackRequestDto feedbackDto) {

		// 1. 프로젝트가 존재하는지
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new IllegalArgumentException("해당하는 프로젝트가 없습니다.")
		);

		// 2. user가 존재하는지
		User user = userRepository.findById(userNo).orElseThrow(
			() -> new IllegalArgumentException("해당하는 사용자가 없습니다.")
		);

		// 3. user 가 팀에 속하는지
		boolean isExist = false;

		//// 해당 프로젝트에 User가 존재하지 않아도 추가가 된다. -> 로직 수정
		Team team = project.getTeam();
		for (TeamUser teamUser : team.getTeamUsers()) {
			if (teamUser.getUser().getNo().equals(userNo)){
				isExist = true;
			}
		}

		if (!isExist){
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
	public FeedbackResponseDto updateFeedback(Long userNo, Long projectNo, Long feedbackNo, FeedbackType feedbackType, FeedbackUpdateRequestDto dto){

		// 1. user가 존재하는지
		User user = userRepository.findById(userNo).orElseThrow(
			() -> new IllegalArgumentException("해당하는 사용자가 없습니다.")
		);

		// 2. 프로젝트가 존재하는지
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new IllegalArgumentException("해당하는 프로젝트가 없습니다.")
		);

		// 3. user 가 팀에 속하는지
		boolean isExist = false;


		Team team = project.getTeam();
		for (TeamUser teamUser : team.getTeamUsers()) {
			if (teamUser.getUser().getNo().equals(userNo)){
				isExist = true;
			}
		}

		if (!isExist){
			throw new IllegalArgumentException("권한이 없습니다.");
		}

		Feedback feedback = feedbackRepository.findById(feedbackNo).orElseThrow(
			() -> new IllegalArgumentException("피드백이 존재하지 않습니다.")
		);


		if (!feedback.getUser().getNo().equals(userNo)){
			throw new IllegalArgumentException("본인의 피드백만 수정할 수 있습니다");
		}

		feedback.updateFeedback(dto.getContent(), feedbackType);

		return new FeedbackResponseDto(feedback);
	}



	@Transactional
	public void deleteFeedback(Long userNo, Long feedbackNo) {
		//본인의 피드백인지
		User user = userRepository.findById(userNo)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다"));

		Feedback feedback = feedbackRepository.findById(feedbackNo).orElseThrow(
				() -> new IllegalArgumentException("피드백이 존재하지 않습니다.")
		);

		if (!feedback.getUser().getNo().equals(userNo)) {
			throw new IllegalArgumentException("본인의 피드백만 삭제할 수 있습니다");
		}

		feedbackRepository.deleteById(feedbackNo);
	}


	@Override
	public Page<FeedbackResponseDto> getFeedbackByUserNo(Long userNo, Pageable pageable) {

		// 1. user가 존재하는지
		User user = userRepository.findById(userNo).orElseThrow(
			() -> new IllegalArgumentException("해당하는 사용자가 없습니다.")
		);

		// 2. user의 feedback 가져오기
		Page<FeedbackResponseDto> feedbackList = feedbackRepository.findFeedbackByUserId(userNo, pageable);

		return feedbackList;
	}


	@Override
	public Page<FeedbackResponseDto> getFeedbackByProjectNo(Long userNo, Long projectNo, Pageable pageable) {

		// 1. 프로젝트가 존재하는지
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new IllegalArgumentException("해당하는 프로젝트가 없습니다.")
		);

		// 2. user 가 팀에 속하는지
		boolean isExist = false;

		Team team = project.getTeam();
		for (TeamUser teamUser : team.getTeamUsers()) {
			if (teamUser.getUser().getNo().equals(userNo)){
				isExist = true;
			}
		}

		if (!isExist){
			throw new IllegalArgumentException("권한이 없습니다.");
		}

		// 3. project feedback 가져오기
		Page<FeedbackResponseDto> feedbackByProjectId = feedbackRepository.findFeedbackByProjectId(projectNo, pageable);

		return feedbackByProjectId;
	}
}
