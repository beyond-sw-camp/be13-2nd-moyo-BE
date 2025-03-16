package com.beyond.backend.domain.team.service;

import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.exception.BaseException;
import com.beyond.backend.domain.common.exception.TeamException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.team.dto.TeamDetailDto;
import com.beyond.backend.domain.team.dto.TeamDto;
import com.beyond.backend.domain.team.dto.TeamMemberListDto;
import com.beyond.backend.domain.team.dto.TeamResponseDto;
import com.beyond.backend.domain.team.entity.Team;
import com.beyond.backend.domain.team.entity.TeamJoinStatus;
import com.beyond.backend.domain.team.repository.TeamRepository;
import com.beyond.backend.domain.teamUser.entity.TeamUser;
import com.beyond.backend.domain.teamUser.repository.TeamUserRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * <p> 팀 서비스 상세
 * 
 * <p>packageName    : com.beyond.backend.service.impl
 * <p>fileName       : TeamServiceImpl
 * <p>author         : hongjm
 * <p>date           : 2025-02-03
 * <p>description    : Team 관련 ServiceImpl
 */
 /*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        hongjm           최초 생성
 * 2025-02-18        hongjm           팀 조회 기능 추가 및 정리
 * 2025-02-20        hongjm           팀 CRUD 정리 및 수정
 * 2025-02-22        hongjm           팀원 추가/제거 등등 기능 추가
 * 2025-02-23        hongjm           기능 추가 및 코드 정리
 * 2025-02-25        hongjm           TeamJoinStatus 수정
 * 2025-02-26        hongjm           중복 코드 통합
 * 2025-03-06        hongjm           로그인 반영
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;
    private final AuthService authService;
    private final NotificationService notificationService;

    
    // 자주 쓰는 유저 찾는 메소드 분리
    private User findUserByUsername() {
        CustomUserDetails userDetails = authService.getCurrentUser();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }

    /* 팀 전체 페이지 페이지*/
    /* 기본적인 CRUD */

    /**
     * 팀 생성
     *
     * @param teamDto 팀 생성 정보
     * @return teamResponseDto
     */

    // timePeriod 객체 삭제  ->  생성자에서 설정 삭제
    @Override
    public TeamResponseDto createTeam(TeamDto teamDto) {
        System.out.println("유저 확인시도");
        User user = findUserByUsername();

        // 팀 저장
        Team team = Team.builder()
                .teamName(teamDto.getTeamName())
                .teamIntroduce(teamDto.getTeamIntroduce())
                .projectStatus(teamDto.getProjectStatus())
                .build();
        team = teamRepository.save(team);

        // TeamUser 저장
        TeamUser teamUser = TeamUser.builder()
                .user(user)
                .team(team)
                .status(TeamJoinStatus.Approved)
                .isLeader(true)
                .build();
        teamUserRepository.save(teamUser);

        return new TeamResponseDto(
                team.getNo(),
                team.getTeamName(),
                team.getTeamIntroduce(),
                team.getProjectStatus()
        );
    }

    /**
     * 팀 정보 수정
     *
     * @param teamDto 팀 정보
     * @throws Exception 팀을 찾을 수 없습니다.
     */
    @Override
    public void updateTeam(TeamResponseDto teamDto) throws Exception {

        Team searchTeam = teamRepository.findById(teamDto.getNo())
                .orElseThrow(() -> new BaseException(ExceptionMessage.TEAM_NOT_FOUND));

        searchTeam.updateTeamDetails(
                teamDto.getTeamName(),
                teamDto.getTeamIntroduce(),
                teamDto.getProjectStatus()
        );

        Team updateTeam = teamRepository.save(searchTeam);

        new TeamResponseDto(
                updateTeam.getNo(),
                updateTeam.getTeamName(),
                updateTeam.getTeamIntroduce(),
                updateTeam.getProjectStatus()
        );
    }

    /**
     * userNo로 팀 정보 조회 서비스
     *
     * @param userNo        (필터) 유저 번호
     * @param teamName      (필터) 팀이름
     * @param teamIntroduce (필터) 팀 설명
     * @param projectStatus (필터) 팀 상태
     * @param page          최소 출력 값
     * @param size          최대 출력 값
     * @return PageImpl 필터링 결과값 반환
     */
    @Override
    public PageImpl<TeamResponseDto> filterUserTeams(
            Long userNo, String teamName, String teamIntroduce, ProjectStatus projectStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeamResponseDto> teams = teamUserRepository.findByUserNoForUserTeams(userNo, pageable);

        List<TeamResponseDto> filteredTeams = teams.stream()
                .filter(team -> teamName == null || team.getTeamName().contains(teamName))
                .filter(team -> teamIntroduce == null || team.getTeamIntroduce().contains(teamIntroduce))
                .filter(team -> projectStatus == null || team.getProjectStatus().equals(projectStatus))
                .toList();
        return new PageImpl<>(filteredTeams, pageable, teams.getTotalElements());
    }

    /**
     * 팀 삭제
     * @param teamNo 팀 ID
     * @throws Exception 팀이 존재하지 않습니다.
     * @throws Exception 권한이 없습니다!
     * @throws Exception 유저가 존재하지 않습니다.
     */
    @Override
    public void deleteTeam(Long teamNo) throws Exception {
        teamRepository.findById(teamNo).orElseThrow(() -> new TeamException(ExceptionMessage.TEAM_NOT_FOUND));

        User user = findUserByUsername();

        Boolean isLeader = teamUserRepository.isLeader(teamNo, user.getNo());
        if (isLeader != null && isLeader) {
            teamRepository.deleteById(teamNo);
        } else {
            throw new UserException(ExceptionMessage.USER_ACCESS_DENIED);
        }
    }

    /* --------- 팀 디테일 페이지 ----------*/

    // @Override
    // public TeamDetailDto getTeamDetailDto(Long teamNo) throws Exception {
    //     return teamRepository.findByTeamDetail(teamNo)
    //             .orElseThrow(() -> new BaseException(ExceptionMessage.TEAM_NOT_FOUND));
    // }

    /**
     * 팀원 목록 조회 서비스
     * @param teamNo 팀번호
     * @return TeamMemberListDto
     * @throws Exception 팀이 존재하지 않습니다.
     */
    @Override
    public List<TeamMemberListDto> getTeamMembers(Long teamNo) throws Exception {
        teamRepository.findById(teamNo)
                .orElseThrow(() -> new BaseException(ExceptionMessage.TEAM_NOT_FOUND));

        return teamUserRepository.findByTeamNoForMember(teamNo,TeamJoinStatus.Approved);
    }

    /**
     * 팀원 신청
     * @param teamNo 팀번호
     * @throws Exception 팀이 존재하지 않습니다.
     * @throws Exception 유저가 존재하지 않습니다.
     * @throws Exception 모집중이 아닙니다!
     * @throws Exception 이미 등록되었거나 요청한 팀 입니다!!
     */
    @Override
    public void teamJoinRequest(Long teamNo) throws Exception {
        User user = findUserByUsername();
        Team team = teamRepository.findById(teamNo)
                .orElseThrow(() -> new BaseException(ExceptionMessage.TEAM_NOT_FOUND));

        if (teamUserRepository.findByUserNoEquals(teamNo, user.getNo())) {
            throw new IllegalArgumentException("이미 등록되었거나 요청한 팀 입니다!!");
        }

        if (team.getProjectStatus() != ProjectStatus.OPEN) {
            throw new IllegalArgumentException("모집중이 아닙니다!");
        }

        User receiver = new User();
        //좋아요 알림///////////////////////////
        List<TeamUser> teamUserList = team.getTeamUsers();
        for (TeamUser teamUser : teamUserList) {
            if (teamUser.isLeader()) {
                receiver = teamUser.getUser();
                break;
            }
        }


        // 트랜잭션 종료 후가 아니라, 바로 알림 전송
        notificationService.sendNotification(
                new RequestNotificationDto(
                        user.getUsername(),
                        receiver.getUsername(),
                        NotificationType.TEAM,
                        user.getUsername() + "님의 팀 가입 신청도착")
        );

        TeamUser teamUser = TeamUser.builder()
                .user(user)
                .team(team)
                .status(TeamJoinStatus.Pending)
                .isLeader(false)
                .build();
        teamUserRepository.save(teamUser);
    }

    /**
     * 팀원 신청 취소
     * @param teamNo 팀번호
     * @throws Exception 신청하지 않았거나 존재하지 않는 팀입니다.
     * @throws Exception 이미 처리된 요청입니다.
     * @throws Exception 팀장 입니다!
     */
    @Override
    public void teamJoinRequestCancel(Long teamNo) throws Exception {
        User user = findUserByUsername();

        Long teamUserNo = teamUserRepository.findByUserNoForTeamUserNo(teamNo, user.getNo());
        TeamJoinStatus status = teamUserRepository.findByTeamStatus(teamNo, user.getNo());
        Boolean isLeader = teamUserRepository.isLeader(teamNo, user.getNo());

        if(isLeader) {
            throw new IllegalArgumentException("팀장 입니다!");
        }

        if (teamUserNo == null) {
            throw new BaseException(ExceptionMessage.TEAM_NOT_FOUND,"신청하지 않았거나 존재하지 않는 팀입니다.");
        } else if (status.equals(TeamJoinStatus.Pending)) {
            teamUserRepository.deleteById(teamUserNo);
        } else{
            throw new BaseException(ExceptionMessage.INVALID_REQUEST);
            // "이미 처리된 요청입니다."
        }
    }

    /**
     * [팀장] 팀원 목록 조회
     * @param teamNo 팀번호
     * @return TeamMemberListDto
     * @throws Exception 권한이 없습니다!
     */
    @Override
    public List<TeamMemberListDto> getTeamMemberRequest(Long teamNo, TeamJoinStatus status) throws Exception {
        User user = findUserByUsername();

        Boolean isLeader = teamUserRepository.isLeader(teamNo, user.getNo());
        if (isLeader != null && isLeader) {
            return teamUserRepository.findByTeamNoForMember(teamNo, status);
        } else {
            throw new UserException(ExceptionMessage.USER_ACCESS_DENIED);
        }
    }

    /**
     * [팀장] 팀원 가입 수락
     * @param teamNo 팀번호
     * @param userNo 신청한 유저의 유저번호
     * @throws Exception 신청한 유저가 없습니다!
     * @throws Exception 이미 가입된 유저입니다!
     */
    @Override
    public void teamAccept(Long teamNo, Long userNo) throws Exception {
        Long teamUserNo = teamUserRepository.findByUserNoForTeamUserNo(teamNo, userNo);

        Team team = teamRepository.findById(teamNo)
                .orElseThrow(() -> new BaseException(ExceptionMessage.TEAM_NOT_FOUND));

        TeamUser teamUser = teamUserRepository.findById(teamUserNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID : " + teamUserNo));
        //"신청한 유저가 없습니다!"
        TeamJoinStatus status = teamUser.getStatus();
        if (status == TeamJoinStatus.Approved) {
            throw new IllegalArgumentException("이미 가입된 유저입니다!");
        }else {

            User sender = userRepository.findById(userNo)
                    .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID : " + userNo));

            User receiver = teamUser.getUser();

            // 트랜잭션 종료 후가 아니라, 바로 알림 전송
            notificationService.sendNotification(
                    new RequestNotificationDto(
                            sender.getUsername(),
                            receiver.getUsername(),
                            NotificationType.TEAM,
                            team.getTeamName() + "팀 가입됨")
            );
            teamUser.setStatus(TeamJoinStatus.Approved);

            teamUserRepository.save(teamUser);
        }
    }

    /**
     * [팀장] 팀원 신청 거부
     * @param teamNo 팀번호
     * @param userNo 신청한 유저의 유저번호
     * @throws Exception 신청한 유저가 없습니다!
     * @throws Exception 팀장 입니다!
     */
    @Override
    public void teamDelete(Long teamNo, Long userNo) throws Exception {
        Long teamUserNo = teamUserRepository.findByUserNoForTeamUserNo(teamNo, userNo);
        Boolean isLeader = teamUserRepository.isLeader(teamNo, userNo);
        if(isLeader) {
            throw new IllegalArgumentException("팀장 입니다!");
        }

        TeamUser teamUser = teamUserRepository.findById(teamUserNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID : " + teamUserNo));
                //.orElseThrow(() -> new IllegalArgumentException("신청한 유저가 없습니다!"));

        teamUser.setStatus(TeamJoinStatus.Rejected);
        teamUserRepository.save(teamUser);
    }

    @Override
    public void teamLeaderSwap(Long teamNo, Long userNo) throws Exception {
        User user = findUserByUsername();
        
        // 팀장 권한 부여
        Long teamUserNo = teamUserRepository.findByUserNoForTeamUserNo(teamNo, userNo);

        TeamUser teamUser = teamUserRepository.findById(teamUserNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID : " + user.getNo()));

        teamUser.setLeader(true);
        teamUserRepository.save(teamUser);

        // 원래 팀장 해임
        teamUserNo = teamUserRepository.findByUserNoForTeamUserNo(teamNo, user.getNo());

        teamUser = teamUserRepository.findById(teamUserNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID : " + user.getNo()));
        teamUser.setLeader(false);
        teamUserRepository.save(teamUser);

    }


    /**
     * 팀 탈퇴
     * @param teamNo 팀번호
     * @param userNo 신청한 유저의 유저번호
     * @throws Exception 신청한 유저가 없습니다!
    @Override
    public void teamDelete(Long teamNo, Long userNo) throws Exception {
        Long teamUserNo = teamUserRepository.findByUserNoForTeamUserNo(teamNo, userNo);

        TeamUser teamUser = teamUserRepository.findById(teamUserNo)
                .orElseThrow(() -> new IllegalArgumentException("신청한 유저가 없습니다!"));

        boolean status = teamUser.isStatus();
        if (!status) {
            teamUserRepository.deleteById(teamUserNo);
        }
    }
    */
}
